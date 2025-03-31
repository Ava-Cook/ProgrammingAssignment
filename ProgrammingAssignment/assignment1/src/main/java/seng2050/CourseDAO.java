package seng2050;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/UniX";
    private static final String USER = "UniX_admin";
    private static final String PASSWORD = "P@ssword1";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL Driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static List<Course> getCoursesBySemester(int semesterID) {
        List<Course> courses = new ArrayList<>();
        String sql = """
            SELECT c.courseID, c.cname, c.credits
            FROM CourseOfferings co
            JOIN Course c ON co.courseID = c.courseID
            WHERE co.semesterID = ?;
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, semesterID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getString("courseID"));
                course.setCourseName(rs.getString("cname"));
                course.setCredits(rs.getInt("credits"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }
}
