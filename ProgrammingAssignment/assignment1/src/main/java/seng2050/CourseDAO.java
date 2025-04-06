package seng2050;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.w3c.dom.Document;

public class CourseDAO {
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
    public static List<Course> getCoursesBySemester(int semesterID) {
        List<Course> courses = new ArrayList<>();
        String sql = """
            SELECT c.courseID, c.cname, c.credits
            FROM CourseOfferings co
            JOIN Course c ON co.courseID = c.courseID
            WHERE co.semesterID = ?;
        """;

        try (Connection conn = datasource.getConnection();
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
