package com.example.payrollmanagementsystem.admin;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Login;
import com.example.payrollmanagementsystem.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UpdateEmployee implements Initializable {
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField mailField;
    @FXML
    private TextField mobileField;
    @FXML
    private TextField provinceField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField branchField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField gradeField;
    @FXML
    private TextField salaryField;
    @FXML
    private TextField ibanField;
    @FXML
    private Button backBtn;

    public void displayProfile(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String query = "call employee_details('%s')".formatted(Login.actionOnEmployeeID);

        try {
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            while (queryResult.next()){
                idField.setText(queryResult.getString("employeeID"));
                nameField.setText(queryResult.getString("name"));
                mailField.setText(queryResult.getString("mail"));
                mobileField.setText(queryResult.getString("mobile"));
                provinceField.setText(queryResult.getString("province"));
                cityField.setText(queryResult.getString("city"));
                branchField.setText(queryResult.getString("branch"));
                titleField.setText(queryResult.getString("title"));
                gradeField.setText(queryResult.getString("gradeID"));
                salaryField.setText(queryResult.getString("salary"));
                ibanField.setText(queryResult.getString("IBAN"));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void updateEmployee(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String query = "UPDATE employees SET name='%s', mail='%s', mobile='%s', province='%s', city='%s', branch='%s', title='%s', gradeID='%s', salary='%s', IBAN='%s' WHERE employeeID = '%s'".
                formatted(nameField.getText(), mailField.getText(), mobileField.getText(), provinceField.getText(), cityField.getText(), branchField.getText(), titleField.getText(), gradeField.getText(), salaryField.getText(), ibanField.getText(), Login.actionOnEmployeeID);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate(query);
            Utility.alertInfo("Employee Information Updated Successfully");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateAdminDashboard(ActionEvent event){
        Utility.navigate(event, "admin/Dashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayProfile();
    }

}
