package com.tcs.maverick.talentdevelopment.beans;

/**
 * Created by abhi on 3/10/2016.
 */
public class ContactsBean {

    private String name;
    private String contactNumber;
    private String email;
    private String role;

    public ContactsBean() {
    }

    public ContactsBean(String name, String contactNumber, String email, String role) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
