package com.tcs.maverick.talentdevelopment.beans;

import java.io.Serializable;

/**
 * Created by abhi on 3/11/2016.
 */
public class CoursesBean implements Serializable{

    private String sessionId;
    private String courseName;
    private String courseDescription;
    private String startDate;
    private String endDate;
    private String time;
    private String isRegistered;
    private String expectedOutcome;
    private String modeOfDelivery;

    public CoursesBean() {
    }


    public CoursesBean(String sessionId, String courseName, String courseDescription, String startDate, String endDate, String time, String isRegistered, String expectedOutcome, String modeOfDelivery) {
        this.sessionId = sessionId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.isRegistered = isRegistered;
        this.expectedOutcome = expectedOutcome;
        this.modeOfDelivery = modeOfDelivery;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getExpectedOutcome() {
        return expectedOutcome;
    }

    public void setExpectedOutcome(String expectedOutcome) {
        this.expectedOutcome = expectedOutcome;
    }

    public String getModeOfDelivery() {
        return modeOfDelivery;
    }

    public void setModeOfDelivery(String modeOfDelivery) {
        this.modeOfDelivery = modeOfDelivery;
    }
}
