package com.tcs.maverick.talentdevelopment.beans;

/**
 * Created by abhi on 3/25/2016.
 */
public class LearningCategoriesBean {
    private int learningCategoryId;
    private String learningCategory;

    public LearningCategoriesBean() {
    }

    public LearningCategoriesBean(int learningCategoryId, String learningCategory) {
        this.learningCategoryId = learningCategoryId;
        this.learningCategory = learningCategory;
    }

    public int getLearningCategoryId() {
        return learningCategoryId;
    }

    public void setLearningCategoryId(int learningCategoryId) {
        this.learningCategoryId = learningCategoryId;
    }

    public String getLearningCategory() {
        return learningCategory;
    }

    public void setLearningCategory(String learningCategory) {
        this.learningCategory = learningCategory;
    }
}
