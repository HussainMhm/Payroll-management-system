package com.example.payrollmanagementsystem.classes;

public class Grade {
    private String gradeID;
    private float basicPay;
    private float salary;
    private float MA;
    private float TA;
    private float WA;

    public Grade(String gradeID, float basicPay, float salary, float MA, float TA, float WA) {
        this.gradeID = gradeID;
        this.basicPay = basicPay;
        this.salary = salary;
        this.MA = MA;
        this.TA = TA;
        this.WA = WA;
    }
}
