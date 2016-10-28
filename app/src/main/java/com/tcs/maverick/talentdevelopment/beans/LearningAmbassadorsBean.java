package com.tcs.maverick.talentdevelopment.beans;

/**
 * Created by abhi on 3/25/2016.
 */
public class LearningAmbassadorsBean {
    private String name, id, email;

    public LearningAmbassadorsBean() {
    }

    public LearningAmbassadorsBean(String name, String id, String email) {
        this.name = name;
        this.id = id;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
