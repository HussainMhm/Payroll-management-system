package com.example.payrollmanagementsystem.admin;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class IssueLeaves implements Initializable {
    @FXML
    private ComboBox<String> employees;
    @FXML
    private Label employeeName;
    @FXML
    private Label employeeBranch;
    @FXML
    private Button selectBtn;

    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Label totalLeaveLabel;
    @FXML
    private TextArea reason;
    @FXML
    private Button issueBtn;

    @FXML
    private Button backBtn;

    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = databaseConnection.getConnection();

    @FXML
    private void leaveButtonOnAction(ActionEvent event) {
        String empID = employees.getSelectionModel().getSelectedItem().toString().split(":")[0];
        LocalDate fDate = fromDate.getValue();
        LocalDate tDate = toDate.getValue();
        String reasonText = reason.getText();

        if (empID.isBlank() || fDate == null || tDate == null || reasonText.isBlank()){
            Utility.alertWarning("Please fill or select all required fields.");
            return;
        }

        String month = fDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int year = fDate.getYear();
        int days = Integer.parseInt(totalLeaveLabel.getText());

        String query = ("INSERT INTO leaves (employeeID, fromDate, toDate, month, year, days, reason, status) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', 'Approved');"
        ).formatted(empID, fDate, tDate, month, year, days, reasonText);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate(query);

            Utility.alertInfo("Leave has been issued successfully");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void selectButtonOnAction(ActionEvent event){
        String employeeID = employees.getSelectionModel().getSelectedItem().toString().split(":")[0];
        String query = "SELECT name, branch FROM employees WHERE employeeID = %s".formatted(employeeID);

        try {
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            while (queryResult.next()){
                employeeName.setText(queryResult.getString("name"));
                employeeBranch.setText(queryResult.getString("branch"));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void datePickerFunction(){
        LocalDate fDate = fromDate.getValue();
        LocalDate tDate = toDate.getValue();
        long numOfDays = tDate.minusDays(fDate.toEpochDay()).toEpochDay();
        int days = (int) numOfDays;

        totalLeaveLabel.setText(String.valueOf(days));
    }

    @FXML
    private void navigateBack(ActionEvent event){
        Utility.navigate(event, "admin/Dashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        ObservableList<String> employeeList = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT employeeID, name FROM employees");

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
