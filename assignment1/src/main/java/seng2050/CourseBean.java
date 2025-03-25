package seng2050;

import java.io.Serializable;

public class CourseBean implements Serializable {
    private String[] courseList;
    
    public CourseBean() {
        this.courseList = new String[]{"Math 101", "CS 202", "History 303", "Physics 404"};
    }
    
    public String[] getCourseList() {
        return courseList;
    }
}

