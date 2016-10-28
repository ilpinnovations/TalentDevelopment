package com.tcs.maverick.talentdevelopment.beans;

/**
 * Created by abhi on 3/13/2016.
 */
public class CompletedCoursesBean {

    private String courseTitle;
    private String completedOn;

    public CompletedCoursesBean() {
    }

    public CompletedCoursesBean(String courseTitle, String completedOn) {
        this.courseTitle = courseTitle;
        this.completedOn = completedOn;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }
}
