package com.example.payrollmanagementsystem.admin;

import com.example.payrollmanagementsystem.DatabaseConnection;
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

public class LeaveRequests implements Initializable {
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    private TextField employeeID;

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
    private Button showAllButton;
    @FXML
    private Button backBtn;
    @FXML
    private Button approveBtn;
    @FXML
    private Button rejectBtn;


    // list to hold the records coming from database
    ObservableList<Leave> requestList = FXCollections.observableArrayList();

    @FXML
    private void showLeaveRequests(){
        requestList.clear();

        query = "SELECT * FROM leaves WHERE status = 'pending'";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                requestList.add(new Leave(
                        resultSet.getString("leaveID"),
                        resultSet.getString("employeeID"),
                        resultSet.getDate("fromDate"),
                        resultSet.getDate("toDate"),
                        resultSet.getInt("days"),
                        resultSet.getString("reason"),
                        resultSet.getString("status")
                ));
                requestsTable.setItems(requestList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateTable(String status){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        Leave leave = requestsTable.getSelectionModel().getSelectedItem();
        String selectedLeaveID = leave.getLeaveID();

        if (status.equals("Approved")){
            query = "UPDATE leaves SET status = '%s' WHERE leaveID = '%s'".formatted(status, selectedLeaveID);
        }
        else if(status.equals("Rejected")){
            query = "UPDATE leaves SET status = '%s' WHERE leaveID = '%s'".formatted(status, selectedLeaveID);
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate(query);
            showLeaveRequests();

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void approveFunction(){
        updateTable("Approved");
        Utility.alertInfo("Request has been approved.");
    }

    @FXML
    private void rejectFunction(){
        updateTable("Rejected");
        Utility.alertInfo("Request has been rejected.");
    }

    @FXML
    private void loadData(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();

        // Enter data to table cells from the database
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
        Utility.navigate(event, "admin/Dashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        showLeaveRequests();
    }
}
