package com.tcs.maverick.talentdevelopment.beans;

/**
 * Created by abhi on 3/27/2016.
 */
public class UpcomingSessionsBean {
    private String sessionId;
    private String sessionName;
    private String startDate;
    private String endDate;
    private String sessionTime;

    public UpcomingSessionsBean() {
    }

    public UpcomingSessionsBean(String sessionId, String sessionName, String startDate, String endDate, String sessionTime) {
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sessionTime = sessionTime;
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

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }
}
