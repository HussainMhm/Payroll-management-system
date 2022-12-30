package com.example.payrollmanagementsystem.admin;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Utility;
import com.example.payrollmanagementsystem.classes.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SalaryReport implements Initializable {
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    private TableView<Payment> paymentsTable;
    @FXML
    private TableColumn<Payment, String> paymentID;
    @FXML
    private TableColumn<Payment, String> empID;
    @FXML
    private TableColumn<Payment, String> name;
    @FXML
    private TableColumn<Payment, Float> amount;
    @FXML
    private TableColumn<Payment, Integer> leave;
    @FXML
    private TableColumn<Payment, String> month;
    @FXML
    private TableColumn<Payment, String> year;

    @FXML
    private ComboBox<String> monthCombo;
    @FXML
    private ComboBox<String> yearCombo;
    @FXML
    private TextField employeeID;

    @FXML
    private Button backBtn;

    // list to hold the records coming from database
    ObservableList<Payment> paymentList = FXCollections.observableArrayList();

    @FXML
    private void showPayments(){
        paymentList.clear();

        query = "SELECT p.paymentID, e.employeeID, e.name, p.amount, p.leave, p.month, p.year " +
                "FROM employees e " +
                "JOIN payments p ON e.employeeID = p.employeeID";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                paymentList.add(new Payment(
                        resultSet.getString("paymentID"),
                        resultSet.getString("employeeID"),
                        resultSet.getString("name"),
                        resultSet.getFloat("amount"),
                        resultSet.getInt("leave"),
                        resultSet.getString("month"),
                        resultSet.getString("year")
                ));
                paymentsTable.setItems(paymentList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void refresh(){
        showPayments();
        employeeID.setText("");
        monthCombo.setValue(null);
        yearCombo.setValue(null);
    }

    @FXML
    private void loadData(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();

        paymentID.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
        empID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        leave.setCellValueFactory(new PropertyValueFactory<>("leave"));
        month.setCellValueFactory(new PropertyValueFactory<>("month"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));

        showPayments();
    }

    @FXML
    private void filterButtonOnAction(){
        String selectedEmployeeID = employeeID.getText();
        String selectedMonth = monthCombo.getSelectionModel().getSelectedItem();
        String selectedYear = yearCombo.getSelectionModel().getSelectedItem();
        if (selectedMonth != null) selectedMonth = selectedMonth.toString();
        if (selectedYear != null) selectedYear = selectedYear.toString();

        String searchQuery;

        if (!selectedEmployeeID.isBlank() && selectedMonth != null && selectedYear != null){
            searchQuery = "SELECT * FROM payments WHERE employeeID = '%s' AND month = '%s' AND year = '%s'".formatted(selectedEmployeeID, selectedMonth, selectedYear);
        }

        else if (!selectedEmployeeID.isBlank() && selectedMonth != null && selectedYear == null){
            searchQuery = "SELECT * FROM payments WHERE employeeID = '%s' AND month = '%s'".formatted(selectedEmployeeID, selectedMonth);
        }

        else if(!selectedEmployeeID.isBlank() && selectedMonth == null && selectedYear == null){
            searchQuery = "SELECT * FROM payments WHERE employeeID = '%s'".formatted(selectedEmployeeID);
        }

        else if (selectedEmployeeID.isBlank() && selectedMonth != null && selectedYear == null){
            searchQuery = "SELECT * FROM payments WHERE month = '%s'".formatted(selectedMonth);
        }

        else if (selectedEmployeeID.isBlank() && selectedMonth == null && selectedYear != null){
            searchQuery = "SELECT * FROM payments WHERE year = '%s'".formatted(selectedYear);
        }

        else{
            searchQuery = "SELECT * FROM payments";
        }

        try {
            paymentList.clear();

            preparedStatement = connection.prepareStatement(searchQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                paymentList.add(new Payment(
                        resultSet.getString("paymentID"),
                        resultSet.getString("employeeID"),
                        resultSet.getString("name"),
                        resultSet.getFloat("amount"),
                        resultSet.getInt("leave"),
                        resultSet.getString("month"),
                        resultSet.getString("year")
                ));
                paymentsTable.setItems(paymentList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateBack(ActionEvent event){
        Utility.navigate(event, "admin/Dashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        ObservableList<String> monthList = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        ObservableList<String> yearList = FXCollections.observableArrayList("2022", "2023");
        monthCombo.setItems(monthList);
        yearCombo.setItems(yearList);
    }
}
