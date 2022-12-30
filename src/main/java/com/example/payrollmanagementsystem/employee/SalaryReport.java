package com.example.payrollmanagementsystem.employee;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Login;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private TableColumn<Payment, Float> amount;
    @FXML
    private TableColumn<Payment, Integer> leave;
    @FXML
    private TableColumn<Payment, String> month;
    @FXML
    private TableColumn<Payment, String> year;

    // list to hold the records coming from database
    ObservableList<Payment> paymentList = FXCollections.observableArrayList();

    @FXML
    private void showPayments(){
        paymentList.clear();

        query = "SELECT * FROM payments WHERE employeeID = '%s'".formatted(Login.userIDIndicator);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                paymentList.add(new Payment(
                        resultSet.getString("paymentID"),
                        resultSet.getString("employeeID"),
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
    private void loadData(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();

        paymentID.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
        empID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        leave.setCellValueFactory(new PropertyValueFactory<>("leave"));
        month.setCellValueFactory(new PropertyValueFactory<>("month"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
    }

    @FXML
    private Button backBtn;

    @FXML
    private void navigateBack(ActionEvent event){
        Utility.navigate(event, "employee/EmployeeDashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        showPayments();
    }
}