package com.example.payrollmanagementsystem.classes;

public class Employee {
    private String employeeID;
    private String name, province, city, mobile, degree, title, branch, grade;
    private float salary;
    private String bankAccount, email, password;

    public Employee(String empID, String name, String province, String mobile, String title, String branch, String grade) {
        this.employeeID = empID;
        this.name = name;
        this.province = province;
        this.mobile = mobile;
        this.title = title;
        this.branch = branch;
        this.grade = grade;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public String getMobile() {
        return mobile;
    }

    public String getTitle() {
        return title;
    }

    public String getBranch() {
        return branch;
    }

    public String getGrade() {
        return grade;
    }
}
