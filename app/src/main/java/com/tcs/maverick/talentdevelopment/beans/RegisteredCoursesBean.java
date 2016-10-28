package com.tcs.maverick.talentdevelopment.beans;

/**
 * Created by abhi on 3/11/2016.
 */
public class RegisteredCoursesBean {

    private String registeredCourseId;
    private String courseTitle;
    private String startDate;
    private String endDate;
    private String time;

    private String isFeedback;
    private String isAttend;
    private String isMultiple;
    private String status;
    private String regId;


    public RegisteredCoursesBean() {
    }

    public RegisteredCoursesBean(String courseTitle, String startDate, String endDate, String time, String isFeedback, String isAttend, String isMultiple, String status, String regId) {
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.isFeedback = isFeedback;
        this.isAttend = isAttend;
        this.isMultiple = isMultiple;
        this.status = status;
        this.regId = regId;
    }

    public RegisteredCoursesBean(String registeredCourseId, String courseTitle, String startDate, String endDate, String time, String isFeedback, String isAttend, String isMultiple, String status, String regId) {
        this.registeredCourseId = registeredCourseId;
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.isFeedback = isFeedback;
        this.isAttend = isAttend;
        this.isMultiple = isMultiple;
        this.status = status;
        this.regId = regId;
    }

    public String getRegisteredCourseId() {
        return registeredCourseId;
    }

    public void setRegisteredCourseId(String registeredCourseId) {
        this.registeredCourseId = registeredCourseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
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

    public String getIsFeedback() {
        return isFeedback;
    }

    public void setIsFeedback(String isFeedback) {
        this.isFeedback = isFeedback;
    }

    public String getIsAttend() {
        return isAttend;
    }

    public void setIsAttend(String isAttend) {
        this.isAttend = isAttend;
    }

    public String getIsMultiple() {
        return isMultiple;
    }

    public void setIsMultiple(String isMultiple) {
        this.isMultiple = isMultiple;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }
}
