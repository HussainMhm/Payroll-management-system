package com.example.payrollmanagementsystem.admin;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    LocalDate todayDate = LocalDate.now();
    String month = todayDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US);

    @FXML
    private ImageView profilePicture;
    @FXML
    private Label activeEmployees;
    @FXML
    private Label leaveRequests;
    @FXML
    private Label totalPayments;
    @FXML
    private Label dateLabel;

    @FXML
    private Button addGradeBtn;
    @FXML
    private Button addEmployeeBtn, employeeReportBtn;
    @FXML
    private Button leaveBtn, leaveReport, requests;
    @FXML
    private Button salaryBtn, salaryReportBtn, yearWiseReportBtn;
    @FXML
    private Button changePasswordBtn;
    @FXML
    private Button logoutBtn;

    private void loadData() throws SQLException {
        String activeEmployeesQuery = "SELECT COUNT(*) FROM employees";
        String leaveRequestsQuery = "SELECT COUNT(*) FROM leaves WHERE status = 'Pending'";
        String totalPaymentsQuery = "SELECT SUM(amount) FROM payments WHERE month = '%s'".formatted(month);

        String activeEmployeesNum = String.valueOf(fetchNumberFromDB(activeEmployeesQuery));
        String leaveRequestsNum = String.valueOf(fetchNumberFromDB(leaveRequestsQuery));
        String totalPaymentsNum = String.valueOf("$" + fetchNumberFromDB(totalPaymentsQuery));

        activeEmployees.setText(activeEmployeesNum);
        leaveRequests.setText(leaveRequestsNum);
        totalPayments.setText(totalPaymentsNum);
        dateLabel.setText(todayDate.toString());
    }

    private int fetchNumberFromDB(String query){
        int result = 0;

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                result = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    @FXML
    private void navigateAddGrade(ActionEvent event){
        Utility.navigate(event, "admin/AddGrade.fxml");
    }

    @FXML
    private void navigateAddEmployee(ActionEvent event){
        Utility.navigate(event, "admin/AddEmployee.fxml");
    }

    @FXML
    private void navigateEmployeeReport(ActionEvent event){
        Utility.navigate(event, "admin/AdminEmployeeReport.fxml");
    }

    @FXML
    private void navigateSalaryPayment(ActionEvent event){
        Utility.navigate(event, "admin/SalaryPayment.fxml");
    }

    @FXML
    private void navigateSalaryReport(ActionEvent event){
        Utility.navigate(event, "admin/SalaryReport.fxml");
    }

    @FXML
    private void navigateLeaveIssue(ActionEvent event){
        Utility.navigate(event, "admin/IssueLeaves.fxml");
    }

    @FXML
    private void navigateLeaveReport(ActionEvent event){
        Utility.navigate(event, "admin/LeaveReport.fxml");
    }

    @FXML
    private void navigateRequests(ActionEvent event){
        Utility.navigate(event, "admin/Requests.fxml");
    }

    @FXML
    private void logout(ActionEvent event){
        Utility.navigate(event, "Login.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}