package com.example.payrollmanagementsystem.classes;

import java.util.Date;

public class Leave {
    private String leaveID;
    private String employeeID;
    private Date fromDate;
    private Date toDate;
    private int days;
    private String reason;
    private String status;

    public Leave(String leaveID, String employee, Date fromDate, Date toDate, int days, String reason, String status) {
        this.leaveID = leaveID;
        this.employeeID = employee;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.days = days;
        this.reason = reason;
        this.status = status;
    }

    public String getLeaveID() {
        return leaveID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public int getDays() {
        return days;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }
}
