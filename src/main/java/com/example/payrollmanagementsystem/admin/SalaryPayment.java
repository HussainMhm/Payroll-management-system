package com.example.payrollmanagementsystem.admin;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SalaryPayment implements Initializable {
    Connection connection = null;
    ResultSet resultSet = null;
    Statement statement = null;

    @FXML
    private ComboBox<String> months;
    @FXML
    private ComboBox<String> years;
    @FXML
    private ComboBox<String> employees;

    @FXML
    private Label employeeLabel, ibanLabel;
    @FXML
    private Label salaryLabel, totalLeaveLabel, leaveDeductionLabel;
    @FXML
    private TextField travelAllowance, medicalAllowance, washingAllowance;
    @FXML
    private Button selectBtn;
    @FXML
    private Button calculateBtn;
    @FXML
    private TextField totalAmount;
    @FXML
    private Button payBtn;
    @FXML
    private Button backBtn;

    @FXML
    private void selectButtonOnAction(ActionEvent event){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();

        if (employees.getSelectionModel().getSelectedItem() == null || months.getSelectionModel().getSelectedItem() == null || years.getSelectionModel().getSelectedItem() == null){
            Utility.alertWarning("Please fill or select all required fields.");
            return;
        }

        String employeeID = employees.getSelectionModel().getSelectedItem().toString().split(":")[0];

        String query = ("SELECT * FROM employees JOIN grades ON employees.gradeID = grades.gradeID " +
                "WHERE employeeID = %s").formatted(employeeID);

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                employeeLabel.setText(resultSet.getString("name"));
                ibanLabel.setText(resultSet.getString("IBAN"));
                salaryLabel.setText(resultSet.getString("employees.salary"));

                travelAllowance.setText(resultSet.getString("TA"));
                medicalAllowance.setText(resultSet.getString("MA"));
                washingAllowance.setText(resultSet.getString("WA"));
                calculateLeaveDaysDeduction();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void calculateButtonOnAction(){
        String salary = salaryLabel.getText();
        String deduction = leaveDeductionLabel.getText();
        String ta = travelAllowance.getText();
        String ma = medicalAllowance.getText();
        String wa = washingAllowance.getText();

        if (salary.isBlank() || deduction.isBlank() || ta.isBlank() || ma.isBlank() || wa.isBlank()){
            Utility.alertWarning("Please select an employee to proceed.");
            return;
        }

        float total = Float.parseFloat(salary) - Float.parseFloat(deduction) + Float.parseFloat(ta) + Float.parseFloat(ma) + Float.parseFloat(wa);
        totalAmount.setText(String.valueOf(total));
    }

    @FXML
    private void calculateLeaveDaysDeduction(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String query = ("SELECT days FROM leaves WHERE employeeID = '%s' AND month = '%s' AND year = '%s'")
                .formatted(
                        employees.getSelectionModel().getSelectedItem().toString().split(":")[0],
                        months.getSelectionModel().getSelectedItem().toString(),
                        years.getSelectionModel().getSelectedItem().toString()
                );

        int monthDays = 28;
        int leavesInMonth = 0;
        float totalDeduction = 0;
        float employeeSalary = Float.parseFloat(salaryLabel.getText());

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                leavesInMonth += Integer.parseInt(resultSet.getString(1));
            }

            totalDeduction = (employeeSalary / monthDays) * leavesInMonth;
            totalLeaveLabel.setText(String.valueOf(leavesInMonth));
            leaveDeductionLabel.setText(String.valueOf(totalDeduction));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void makePaymentOnAction(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        if (totalAmount.getText().isBlank()){
            Utility.alertWarning("Select an employee and calculate the amount to be paid.");
            return;
        }

        try{
            String paymentQuery = "INSERT INTO payments (employeeID, amount, `leave`, `month`, `year`) " +
                            "VALUES ('%s', '%s', '%s', '%s', '%s');"
            .formatted(
                    employees.getSelectionModel().getSelectedItem().toString().split(":")[0], totalAmount.getText(), totalLeaveLabel.getText(),
                    months.getSelectionModel().getSelectedItem().toString(),
                    years.getSelectionModel().getSelectedItem().toString()
            );

            PreparedStatement preparedStatement = connection.prepareStatement(paymentQuery);
            preparedStatement.executeUpdate(paymentQuery);

            Utility.alertInfo("Payment is done successfully to employee's bank account.");
            emptyFields();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void emptyFields(){
        employees.setValue(null);
        months.setValue(null);
        years.setValue(null);
        employeeLabel.setText("");
        ibanLabel.setText("");
        salaryLabel.setText("");
        totalLeaveLabel.setText("");
        leaveDeductionLabel.setText("");
        travelAllowance.setText("");
        medicalAllowance.setText("");
        washingAllowance.setText("");
        totalAmount.setText("");
    }

    @FXML
    private void navigateBack(ActionEvent event){
        Utility.navigate(event, "admin/Dashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        ObservableList<String> monthList = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        months.setItems(monthList);

        ObservableList<String> yearList = FXCollections.observableArrayList("2022", "2023");
        years.setItems(yearList);

        ObservableList<String> employeeList = FXCollections.observableArrayList();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT employeeID, name FROM employees");

            while (resultSet.next()){
                int id = resultSet.getInt("employeeID");
                String name = resultSet.getString("name");
                employeeList.add(id + ": " + name);
            }
            employees.setItems(employeeList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
