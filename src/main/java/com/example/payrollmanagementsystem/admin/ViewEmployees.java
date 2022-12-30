package com.example.payrollmanagementsystem.admin;

import com.example.payrollmanagementsystem.DatabaseConnection;
import com.example.payrollmanagementsystem.Login;
import com.example.payrollmanagementsystem.Utility;
import com.example.payrollmanagementsystem.classes.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ViewEmployees implements Initializable {
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    private TableView<Employee> employeesTable;
    @FXML
    private TableColumn<Employee, String> employeeID;
    @FXML
    private TableColumn<Employee, String> name;
    @FXML
    private TableColumn<Employee, String> province;
    @FXML
    private TableColumn<Employee, String> mobile;
    @FXML
    private TableColumn<Employee, String> title;
    @FXML
    private TableColumn<Employee, String> branch;
    @FXML
    private TableColumn<Employee, String> grade;

    @FXML
    private Button backBtn;

    // list to hold the records coming from database
    ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    private void showUsers(){
        employeeList.clear();

        query = "SELECT * FROM employees";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                employeeList.add(new Employee(
                        resultSet.getString("employeeID"),
                        resultSet.getString("name"),
                        resultSet.getString("province"),
                        resultSet.getString("mobile"),
                        resultSet.getString("title"),
                        resultSet.getString("branch"),
                        resultSet.getString("gradeID")));
                employeesTable.setItems(employeeList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editEmployee(ActionEvent event){
        Employee employee = employeesTable.getSelectionModel().getSelectedItem();

        if (employee==null) {
            Utility.alertWarning("Please select an employee to update.");
        }

        Login.actionOnEmployeeID = employee.getEmployeeID();
        Utility.navigate(event, "admin/UpdateEmployee.fxml");
    }

    @FXML
    private void deleteEmployee(){
        Employee employee = employeesTable.getSelectionModel().getSelectedItem();

        if (employee==null) {
            Utility.alertWarning("Please select an employee to delete.");
        }
        else {
            query = "DELETE FROM employees WHERE employeeID = '%s'".formatted(employee.getEmployeeID());
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();

                Utility.alertInfo("Employee deleted successfully");

                showUsers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void loadData() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();

        employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        province.setCellValueFactory(new PropertyValueFactory<>("province"));
        mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        branch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        grade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        showUsers();
    }

    @FXML
    private void navigateBack(ActionEvent event){
        Utility.navigate(event, "admin/Dashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }
}
