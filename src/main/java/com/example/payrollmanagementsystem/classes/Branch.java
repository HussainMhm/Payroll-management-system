package com.example.payrollmanagementsystem.classes;

public class Branch {
    private int BranchID;
    private String BranchName;

    public Branch(int branchID, String branchName) {
        BranchID = branchID;
        BranchName = branchName;
    }

    public int getBranchID() {
        return BranchID;
    }

    public void setBranchID(int branchID) {
        BranchID = branchID;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }
}
