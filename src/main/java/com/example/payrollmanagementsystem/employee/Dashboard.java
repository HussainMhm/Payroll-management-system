package com.example.payrollmanagementsystem.employee;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Login;
import com.example.payrollmanagementsystem.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    private Label empNameLabel;
    @FXML
    private Label empIDLabel;
    @FXML
    private Label empDepartmentLabel;
    @FXML
    private Label empGradeLabel;

    @FXML
    private Button requestLeaveBtn;
    @FXML
    private Button requestStatusBtn;
    @FXML
    private Button salaryReportsBtn;
    @FXML
    private Button changePasswordBtn;

    @FXML
    private Button logoutBtn;

    private void loadData() throws SQLException {
        String employeeID = Login.userIDIndicator;

        String empDepartmentQuery = "SELECT branch FROM employees WHERE employeeID = %s".formatted(employeeID);
        String empGradeQuery = "SELECT gradeID FROM employees WHERE employeeID = %s".formatted(employeeID);

        String employeeDepartment = fetchStringFromDB(empDepartmentQuery);
        String employeeGrade = fetchStringFromDB(empGradeQuery);

        empNameLabel.setText(fetchStringFromDB("SELECT name From employees WHERE employeeID ='%s'".formatted(employeeID)));
        empIDLabel.setText(employeeID);
        empDepartmentLabel.setText(employeeDepartment);
        empGradeLabel.setText(employeeGrade);
    }

    private String fetchStringFromDB(String query){
        String result = null;

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                result = resultSet.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    @FXML
    private void navigateRequestLeave(ActionEvent event){
        Utility.navigate(event, "employee/EmployeeRequestLeave.fxml");
    }

    @FXML
    private void navigateRequestStatus(ActionEvent event){
        Utility.navigate(event, "employee/EmployeeRequestStatus.fxml");
    }

    @FXML
    private void navigateSalaryReport(ActionEvent event){
        Utility.navigate(event, "employee/EmployeeSalaryReport.fxml");
    }

    @FXML
    private void navigateChangePassword(ActionEvent event){
        Utility.navigate(event, "employee/EmployeeChangePassword.fxml");
    }

    @FXML
    private void navigateProfile(ActionEvent event){
        Utility.navigate(event, "employee/EmployeeProfile.fxml");
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
