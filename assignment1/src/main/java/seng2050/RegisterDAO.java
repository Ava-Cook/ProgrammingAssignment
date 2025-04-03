package seng2050;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.w3c.dom.Document;

public class RegisterDAO {

  private static DataSource datasource;

    static {

        try {
            // Reading database configuration parameters    
            File file = new File("databaseConfig.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            String jdbc = document.getElementsByTagName("jdbcDriver").item(0).getTextContent();
            String databaseURL = document.getElementsByTagName("databaseURL").item(0).getTextContent();
            String usr = document.getElementsByTagName("user").item(0).getTextContent();
            String pwd = document.getElementsByTagName("password").item(0).getTextContent();
     
            // Setting the connection pool properties
            PoolProperties p = new PoolProperties();
            p.setUrl(databaseURL);
            p.setDriverClassName(jdbc);
            p.setUsername(usr);
            p.setPassword(pwd);
            // You can set additional pool properties
            p.setMaxActive(100); // Maximum number of connections in the pool
    
            // Setting the data source with the pool properties defined above
            datasource = new DataSource();
            datasource.setPoolProperties(p);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    // Register student for a course (with credit limit check)
public static boolean registerStudent(String studentNo, String courseID, int semesterID) {
    try (Connection conn = datasource.getConnection()) {
        conn.setAutoCommit(false); // Start transaction

        // Step 1: Get total enrolled credits for this student in the semester
        String totalCreditsQuery = "SELECT SUM(c.credits) FROM StudentCourseRegistration r " +
                                   "JOIN Course c ON r.courseID = c.courseID " +
                                   "WHERE r.stdNo = ? AND r.semesterID = ?";
        PreparedStatement totalCreditsStmt = conn.prepareStatement(totalCreditsQuery);
        totalCreditsStmt.setString(1, studentNo);
        totalCreditsStmt.setInt(2, semesterID);
        ResultSet totalCreditsRs = totalCreditsStmt.executeQuery();
        
        int currentCredits = 0;
        if (totalCreditsRs.next() && totalCreditsRs.getInt(1) != 0) {
            currentCredits = totalCreditsRs.getInt(1);
        }
        totalCreditsStmt.close();

        // Step 2: Get the credits of the new course
        String courseCreditsQuery = "SELECT credits FROM Course WHERE courseID = ?";
        PreparedStatement courseCreditsStmt = conn.prepareStatement(courseCreditsQuery);
        courseCreditsStmt.setString(1, courseID);
        ResultSet courseCreditsRs = courseCreditsStmt.executeQuery();
        
        int newCourseCredits = 0;
        if (courseCreditsRs.next()) {
            newCourseCredits = courseCreditsRs.getInt(1);
        }
        courseCreditsStmt.close();

        // Step 3: Check if enrollment would exceed 40 credits
        if (currentCredits + newCourseCredits > 40) {
            throw new Exception("Cannot enroll. Maximum credit limit (40) exceeded.");
        }

        // Step 4: Proceed with registration if credits are within limit
        String insertQuery = "INSERT INTO StudentCourseRegistration (stdNo, courseID, semesterID, grade, mark) VALUES (?, ?, ?, NULL, NULL)";
        PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
        insertStmt.setString(1, studentNo);
        insertStmt.setString(2, courseID);
        insertStmt.setInt(3, semesterID);

        boolean success = insertStmt.executeUpdate() > 0;
        conn.commit(); // Commit transaction
        return success;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    // Get the list of courses a student is registered for
    public static List<Course> getRegisteredCourses(String studentNo, int semesterID) {
        List<Course> courses = new ArrayList<>();
        try (Connection conn = datasource.getConnection()) {
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
