package com.example.payrollmanagementsystem.classes;

public class Payment {
    private String paymentID;
    private String employeeID;
    private String employeeName;
    private float amount;
    private String iban;
    private int leave;
    private String month;
    private String year;


    public Payment(String paymentID, String employeeID, float amount, int leave, String month, String year) {
        this.paymentID = paymentID;
        this.employeeID = employeeID;
        this.amount = amount;
        this.leave = leave;
        this.month = month;
        this.year = year;
    }

    public Payment(String paymentID, String employeeID, String employeeName, float amount, int leave, String month, String year) {
        this.paymentID = paymentID;
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.amount = amount;
        this.leave = leave;
        this.month = month;
        this.year = year;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return employeeName;
    }

    public float getAmount() {
        return amount;
    }

    public String getIban() {
        return iban;
    }

    public int getLeave() {
        return leave;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}
