package seng2050;

import java.io.Serializable;

public class Semester implements Serializable {
    private int semesterID;
    private int semester;
    private int year;

    public Semester() {}

    public Semester(int semesterID, int semester, int year) {
        this.semesterID = semesterID;
        this.semester = semester;
        this.year = year;
    }

    public int getSemesterID() {
        return semesterID;
    }

    public void setSemesterID(int semesterID) {
        this.semesterID = semesterID;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
