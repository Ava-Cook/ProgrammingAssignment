package seng2050;

import java.io.Serializable;

public class SemesterBean implements Serializable {
    private String[] semesterList;

    public String[] getSemesterList() {
        return semesterList;
    }

    public void setSemesterList(String[] semesterList) {
        this.semesterList = semesterList;
    }
}