package com.tcs.maverick.talentdevelopment.beans;

/**
 * Created by abhi on 3/13/2016.
 */
public class MultipleDayRegisteredCoursesBean {
    private String sessionId;
    private String sessionName;
    private String time;
    private String date;
    private String isAttend;
    private String isFeedback;

    public MultipleDayRegisteredCoursesBean() {
    }

    public MultipleDayRegisteredCoursesBean(String sessionId, String sessionName, String time, String date, String isAttend, String isFeedback) {
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.time = time;
        this.date = date;
        this.isAttend = isAttend;
        this.isFeedback = isFeedback;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsAttend() {
        return isAttend;
    }

    public void setIsAttend(String isAttend) {
        this.isAttend = isAttend;
    }

    public String getIsFeedback() {
        return isFeedback;
    }

    public void setIsFeedback(String isFeedback) {
        this.isFeedback = isFeedback;
    }
}
