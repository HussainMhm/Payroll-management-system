package com.example.payrollmanagementsystem.employee;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Login;
import com.example.payrollmanagementsystem.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class RequestLeave {
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Label totalLeaveLabel;
    @FXML
    private TextArea reason;
    @FXML
    private Button issueBtn;
    @FXML
    private Button backBtn;

    @FXML
    private void leaveButtonOnAction(ActionEvent event) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String empID =  Utility.getEmployeeIdFromMail(Login.userMailIndicator);
        LocalDate fDate = fromDate.getValue();
        LocalDate tDate = toDate.getValue();
        String reasonText = reason.getText();

        if (empID.isBlank() || fDate == null || tDate == null || reasonText.isBlank()){
            Utility.alertWarning("Please fill or select all required fields.");
            return;
        }

        String month = fDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int year = fDate.getYear();
        int days = Integer.parseInt(totalLeaveLabel.getText());

        String query = (
                "INSERT INTO leaves (employeeID, fromDate, toDate, month, year, days, reason, status) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', 'Pending');"
        ).formatted(empID, fDate, tDate, month, year, days, reasonText);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate(query);

            Utility.alertInfo("Leave Request has been sent to the management.");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void datePickerFunction(){
        LocalDate fDate = fromDate.getValue();
        LocalDate tDate = toDate.getValue();
        long numOfDays = tDate.minusDays(fDate.toEpochDay()).toEpochDay();
        int days = (int) numOfDays;

        totalLeaveLabel.setText(String.valueOf(days));
    }

    @FXML
    private void navigateBack(ActionEvent event){
        Utility.navigate(event, "employee/EmployeeDashboard.fxml");
    }
}
