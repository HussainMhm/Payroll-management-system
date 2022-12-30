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

public class LeaveReport implements Initializable {
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    private TableView<Leave> leavesTable;
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
    private ComboBox<String> monthCombo;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private TextField employeeID;

    @FXML
    private Button backBtn;

    // list to hold the records coming from database
    ObservableList<Leave> leaveList = FXCollections.observableArrayList();

    @FXML
    private void showLeaves(){
        leaveList.clear();
        query = "SELECT * FROM leaves";

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
                leavesTable.setItems(leaveList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void refresh(){
        showLeaves();
        employeeID.setText("");
        monthCombo.setValue(null);
        statusCombo.setValue(null);
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

        showLeaves();
    }

    @FXML
    private void filterButtonOnAction(){
        String selectedEmployeeID = employeeID.getText();
        String selectedMonth = monthCombo.getSelectionModel().getSelectedItem();
        String selectedStatus = statusCombo.getSelectionModel().getSelectedItem();
        if (selectedMonth != null) selectedMonth = selectedMonth.toString();
        if (selectedStatus != null) selectedStatus = selectedStatus.toString();

        String searchQuery;

        if (!selectedEmployeeID.isBlank() && selectedMonth != null && selectedStatus != null)
            searchQuery = "SELECT * FROM leaves WHERE employeeID = '%s' AND month = '%s' AND status = '%s'".formatted(selectedEmployeeID, selectedMonth, selectedStatus);

        else if (!selectedEmployeeID.isBlank() && selectedMonth == null && selectedStatus == null)
            searchQuery = "SELECT * FROM leaves WHERE employeeID = '%s'".formatted(selectedEmployeeID);

        else if (selectedEmployeeID.isBlank() && selectedMonth != null && selectedStatus == null)
            searchQuery = "SELECT * FROM leaves WHERE month = '%s'".formatted(selectedMonth);

        else if (selectedEmployeeID.isBlank() && selectedMonth == null && selectedStatus != null)
            searchQuery = "SELECT * FROM leaves WHERE status = '%s'".formatted(selectedStatus);
        else{
            Utility.alertWarning("Select the required fields");
            searchQuery = "SELECT * FROM leaves";
        }


        try {
            leaveList.clear();

            preparedStatement = connection.prepareStatement(searchQuery);
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
                leavesTable.setItems(leaveList);
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
        ObservableList<String> statusList = FXCollections.observableArrayList("Approved", "Pending", "Rejected");
        monthCombo.setItems(monthList);
        statusCombo.setItems(statusList);
    }
}
