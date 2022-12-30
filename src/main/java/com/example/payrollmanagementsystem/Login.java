package com.example.payrollmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login {
    @FXML
    private RadioButton rAdmin, rEmployee;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginBtn;

    public static String userMailIndicator = null;
    public static String userIDIndicator = null;
    public static String actionOnEmployeeID = null;

    public void onEnter(ActionEvent ae){
        loginBtn.fire();
    }

    public void loginButtonOnAction(ActionEvent event){
        // Fields filled
        if (!username.getText().isBlank() && !password.getText().isBlank())
            validateLogin(event);
        // Some fields missing
        else
            Utility.alertWarning("Please enter username and password.");
    }

    public void validateLogin(ActionEvent event){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String getUsername = username.getText();
        String getPassword = password.getText();

        userMailIndicator = getUsername;
        userIDIndicator = Utility.getEmployeeIdFromMail(userMailIndicator);

        String getType = "";
            if (rAdmin.isSelected()) getType = rAdmin.getText().toLowerCase();
            else if (rEmployee.isSelected()) getType = rEmployee.getText().toLowerCase();

        String verifyLogin = "SELECT count(1) FROM users where username = '%s' AND password = '%s' AND type = '%s'"
                .formatted(getUsername, getPassword, getType);

        try {
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            if (getType.equals("admin")){
                if (getUsername.equals("admin") && getPassword.equals("admin"))
                    navigateAdminPage(event);
                else
                    Utility.alertError("You have entered an invalid username or password");

            }
            else if(getType.equals("employee")){
                while (queryResult.next()) {
                    if (queryResult.getInt(1) == 1) {
                        navigateEmployeePage(event);
                    }
                    else {
                        Utility.alertError("You have entered an invalid username or password");
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateAdminPage(ActionEvent event){
        Utility.navigate(event, "admin/Dashboard.fxml");
    }

    @FXML
    private void navigateEmployeePage(ActionEvent event){
        Utility.navigate(event, "employee/EmployeeDashboard.fxml");
    }
}
