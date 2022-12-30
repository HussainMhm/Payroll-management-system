module com.example.payrollmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.payrollmanagementsystem to javafx.fxml;
    exports com.example.payrollmanagementsystem;
    exports com.example.payrollmanagementsystem.classes;
    opens com.example.payrollmanagementsystem.classes to javafx.fxml;
    exports com.example.payrollmanagementsystem.employee;
    opens com.example.payrollmanagementsystem.employee to javafx.fxml;
    exports com.example.payrollmanagementsystem.admin;
    opens com.example.payrollmanagementsystem.admin to javafx.fxml;
}