package com.tcs.maverick.talentdevelopment.beans;

/**
 * Created by abhi on 3/27/2016.
 */
public class CompletedSessionsBean {
    private String sessionId;
    private String sessionName;
    private String completedOn;

    public CompletedSessionsBean() {
    }

    public CompletedSessionsBean(String sessionId, String sessionName, String completedOn) {
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.completedOn = completedOn;
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

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }
}
