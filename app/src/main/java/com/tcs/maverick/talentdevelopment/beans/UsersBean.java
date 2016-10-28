package com.tcs.maverick.talentdevelopment.beans;

/**
 * Created by abhi on 3/27/2016.
 */
public class UsersBean {
    private String empId;
    private String empName;
    private String empEmail;

    public UsersBean() {
    }

    public UsersBean(String empId, String empName, String empEmail) {
        this.empId = empId;
        this.empName = empName;
        this.empEmail = empEmail;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }
}
