package seng2050;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RegisterDAO {

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

    // Get the database connection
    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Register student for a course
    public static boolean registerStudent(String studentNo, String courseID, int semesterID) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO StudentCourseRegistration (stdNo, courseID, semesterID, grade, mark) VALUES (?, ?, ?, NULL, NULL)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentNo);
            stmt.setString(2, courseID);
            stmt.setInt(3, semesterID);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get the list of courses a student is registered for
    public static List<Course> getRegisteredCourses(String studentNo, int semesterID) {
        List<Course> courses = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT c.courseID, c.cname, c.credits FROM Course c " +
                         "JOIN StudentCourseRegistration r ON c.courseID = r.courseID " +
                         "WHERE r.stdNo = ? AND r.semesterID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentNo);
            stmt.setInt(2, semesterID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(new Course(rs.getString("courseID"), rs.getString("cname"), rs.getInt("credits")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }
}
