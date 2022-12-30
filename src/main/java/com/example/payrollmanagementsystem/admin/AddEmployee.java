package com.example.payrollmanagementsystem.admin;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Utility;
import com.example.payrollmanagementsystem.classes.Grade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddEmployee implements Initializable {
    @FXML
    private TextField name, province, city, mobile, title;
    @FXML
    private ComboBox<String> degreeCombo;
    @FXML
    private ComboBox<String> branchCombo;
    @FXML
    private ComboBox<String> gradeCombo;
    @FXML
    private TextField salary, iban, mail;
    @FXML
    private PasswordField password;
    @FXML
    private Button addEmployeeBtn;
    @FXML
    private Button backBtn;

    @FXML
    private void addEmployeeOnAction(ActionEvent event){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String nameS = name.getText();
        String mailS = mail.getText();
        String mobileS = mobile.getText();
        String cityS = city.getText();
        String provinceS = province.getText();
        String degreeS = degreeCombo.getSelectionModel().getSelectedItem();
        String titleS = title.getText();
        String branchS = branchCombo.getSelectionModel().getSelectedItem();
        String gradeS = gradeCombo.getSelectionModel().getSelectedItem();
        String salaryS = salary.getText();
        String ibanS = iban.getText();

        if (nameS.isBlank() || mailS.isBlank() || mobileS.isBlank() || cityS.isBlank() || provinceS.isBlank() || degreeS == null || titleS.isBlank() || branchS == null || gradeS == null || salaryS.isBlank() || ibanS.isBlank()) {
            Utility.alertError("Please fill or select all required fields.");
            return;
        }

        String newEmployeeQuery = (
                "INSERT INTO employees (name, mail, mobile, province, city, degree, title, branch, gradeID, salary, IBAN) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');")
                .formatted(nameS, mailS, mobileS, provinceS, cityS, degreeS.toString(), titleS, branchS.toString(), gradeS.toString(), salaryS, ibanS);

        String newUserQuery = (
                "INSERT INTO users (username, password, type) VALUES ('%s', '%s', '%s');"
        ).formatted(mail.getText(), password.getText(), "employee");

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(newEmployeeQuery);
            preparedStatement.executeUpdate(newEmployeeQuery);
            Utility.alertInfo("Employee Added Successfully");

            preparedStatement = connection.prepareStatement(newUserQuery);
            preparedStatement.executeUpdate(newUserQuery);

        } catch (SQLException e){
            e.printStackTrace();
        }
        }

    @FXML
    private void navigateBack(ActionEvent event){
        Utility.navigate(event, "admin/Dashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> degreeList = FXCollections.observableArrayList("Bachelor", "Master", "Doctorate", "Other");
        ObservableList<String> branchList = FXCollections.observableArrayList("Accounting", "Customer Service", "Human Resource", "IT", "Management", "Marketing");
        ObservableList<String> gradeList = FXCollections.observableArrayList("Grade I", "Grade II", "Grade III", "Grade IV", "Grade V");
        degreeCombo.setItems(degreeList);
        branchCombo.setItems(branchList);
        gradeCombo.setItems(gradeList);
    }
}
