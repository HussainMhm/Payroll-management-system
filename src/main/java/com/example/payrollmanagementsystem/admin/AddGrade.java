package com.example.payrollmanagementsystem.admin;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.*;

public class AddGrade {
    @FXML
    private TextField gradeID, salary, travelAllowance, medicalAllowance, washingAllowance;
    @FXML
    private Button AddGradeBtn;
    @FXML
    private Button backBtn;

    @FXML
    private void addGradeOnAction(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String getGradeID = gradeID.getText();
        String getSalary = salary.getText();
        String getTA = travelAllowance.getText();
        String getMA = medicalAllowance.getText();
        String getWA = washingAllowance.getText();

        if (getGradeID.isBlank() || getSalary.isBlank() || getTA.isBlank() || getMA.isBlank() || getWA.isBlank()){
            Utility.alertWarning("Please fill or select all required fields.");
            return;
        }

        String query = (
                "INSERT INTO grades (gradeID, salary, MA, TA, WA) " +
                "VALUES ('%s', %s, %s, %s, %s);"
        ).formatted(getGradeID, getSalary, getMA, getTA, getWA);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate(query);
            Utility.alertInfo("Grade Added Successfully");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateBack(ActionEvent event){
        Utility.navigate(event, "admin/Dashboard.fxml");
    }
}