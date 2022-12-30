package com.example.payrollmanagementsystem.employee;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Login;
import com.example.payrollmanagementsystem.Utility;
import com.example.payrollmanagementsystem.classes.Leave;
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

public class RequestStatus implements Initializable {
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    private TableView<Leave> requestsTable;
    @FXML
    private TableColumn<Leave, String> leaveID;
    @FXML
    private TableColumn<Leave, String> empID;
    @FXML
    private TableColumn<Leave, Date> fromDate;
    @FXML
    private TableColumn<Leave, Date> toDate;
    @FXML
    private TableColumn<Leave, Integer> days;
    @FXML
    private TableColumn<Leave, String> reason;
    @FXML
    private TableColumn<Leave, String> status;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button backBtn;

    // list to hold the records coming from database
    ObservableList<Leave> leaveList = FXCollections.observableArrayList();

    @FXML
    private void showLeaves(){
        leaveList.clear();

        query = "SELECT * FROM leaves WHERE employeeID = '%s'".formatted(Login.userIDIndicator);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                leaveList.add(new Leave(
                        resultSet.getString("leaveID"),
                        resultSet.getString("employeeID"),
                        resultSet.getDate("fromDate"),
                        resultSet.getDate("toDate"),
                        resultSet.getInt("days"),
                        resultSet.getString("reason"),
                        resultSet.getString("status")
                ));
                requestsTable.setItems(leaveList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadData(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();

        leaveID.setCellValueFactory(new PropertyValueFactory<>("leaveID"));
        empID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        fromDate.setCellValueFactory(new PropertyValueFactory<>("fromDate"));
        toDate.setCellValueFactory(new PropertyValueFactory<>("toDate"));
        days.setCellValueFactory(new PropertyValueFactory<>("days"));
        reason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private void navigateBack(ActionEvent event){
        Utility.navigate(event, "employee/EmployeeDashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        showLeaves();
    }
}
