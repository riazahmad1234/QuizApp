package com.example.quizapp.Model;

public class Subject_Model {
    private String subjectTitle;

    public Subject_Model(){

    }
    public Subject_Model(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }
}
