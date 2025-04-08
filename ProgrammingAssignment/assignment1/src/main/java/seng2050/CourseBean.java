package seng2050;

import java.util.List;
//makes a list of courses
public class CourseBean {
    private List<Course> courses;

    public CourseBean(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}

