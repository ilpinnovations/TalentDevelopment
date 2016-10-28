package com.tcs.maverick.talentdevelopment.beans;

/**
 * Created by abhi on 3/17/2016.
 */
public class LeadersBean {
    private String leaderName;
    private String leaderScore;

    public LeadersBean() {
    }

    public LeadersBean(String leaderName, String leaderScore) {
        this.leaderName = leaderName;
        this.leaderScore = leaderScore;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderScore() {
        return leaderScore;
    }

    public void setLeaderScore(String leaderScore) {
        this.leaderScore = leaderScore;
    }
}
