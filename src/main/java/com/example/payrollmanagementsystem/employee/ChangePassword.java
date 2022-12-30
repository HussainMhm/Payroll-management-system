package com.example.payrollmanagementsystem.employee;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Login;
import com.example.payrollmanagementsystem.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangePassword {
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button backBtn;

    @FXML
    private void changePassword(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String query = null;

        String newPass = newPasswordField.getText();
        String repeatPass = repeatPasswordField.getText();

        if (newPass.isBlank() || repeatPass.isBlank()){
            Utility.alertWarning("Please fill the required fields");
            return;
        }

        if (newPass.equals(repeatPass)){
            query = "UPDATE users SET password = '%s' WHERE username = '%s'".formatted(newPass, Login.userMailIndicator);
        }
        else {
            Utility.alertError("Passwords entered do not match.");
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate(query);
            Utility.alertInfo("Password Updated Successfully");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateBack(ActionEvent event){
        Utility.navigate(event, "employee/EmployeeDashboard.fxml");
    }
}
