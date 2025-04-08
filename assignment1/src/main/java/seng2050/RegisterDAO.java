package seng2050;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

    //map the full courses for the semester chosen
    public static Map<String, Boolean> getFullCoursesForSemester(int semesterID) {
        Map<String, Boolean> fullCourses = new HashMap<>();

        //database information
        try (Connection conn = datasource.getConnection()) {
            String sql = "SELECT co.courseID, co.maxCapacity, COUNT(scr.stdNo) AS enrolled " +
                         "FROM CourseOfferings co " +
                         "LEFT JOIN StudentCourseRegistration scr ON co.courseID = scr.courseID AND co.semesterID = scr.semesterID " +
                         "WHERE co.semesterID = ? " +
                         "GROUP BY co.courseID, co.maxCapacity";
    
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, semesterID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String courseID = rs.getString("courseID");
                int maxCapacity = rs.getInt("maxCapacity");
                int enrolled = rs.getInt("enrolled");
                fullCourses.put(courseID, enrolled >= maxCapacity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return fullCourses;
    } 

    //get the total number of credits a student is registered for
    public static int getTotalCredits(String studentNo, int semesterID) {
        int totalCredits = 0;

        try (Connection conn = datasource.getConnection()) {
            String sql = "SELECT SUM(c.credits) FROM StudentCourseRegistration r " +
                         "JOIN Course c ON r.courseID = c.courseID " +
                         "WHERE r.stdNo = ? AND r.semesterID = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentNo);
            stmt.setInt(2, semesterID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalCredits = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCredits;
    }

    // Check if the student has completed the required assumed knowledge: get a list of the courses that have assumed knowledge that the student has not completed
    public static List<String> getCoursesWithUnmetAssumedKnowledge(String studentNo, String[] selectedCourses) {
        List<String> unmetCourses = new ArrayList<>();

        //database data for courses with assumedknoledge and a list of assumedknowledges
        try (Connection conn = datasource.getConnection()) {
            for (String courseID : selectedCourses) {
                String assumedQuery = "SELECT assumedKnowledge FROM AssumedKnowledge WHERE courseID = ?";
                PreparedStatement assumedStmt = conn.prepareStatement(assumedQuery);
                assumedStmt.setString(1, courseID);
                ResultSet rs = assumedStmt.executeQuery();
    
                while (rs.next()) {
                    String requiredID = rs.getString("assumedKnowledge");
                     //database data for courses with assumedknoledge and student registration information to check if a student has compleated those classes
                    String checkCompletion = "SELECT COUNT(*) FROM StudentCourseRegistration " +
                                             "WHERE stdNo = ? AND courseID = ? AND grade IN ('P', 'C', 'D', 'HD')";
                    PreparedStatement compStmt = conn.prepareStatement(checkCompletion);
                    compStmt.setString(1, studentNo);
                    compStmt.setString(2, requiredID);
                    ResultSet checkRs = compStmt.executeQuery();
    
                    if (checkRs.next() && checkRs.getInt(1) == 0 && !unmetCourses.contains(courseID)) {
                        unmetCourses.add(courseID);
                        break;
                    }
    
                    compStmt.close();
                }
    
                assumedStmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unmetCourses;
    }
    // Check if the student has completed the required prerequisite knowledge: get a list of the courses that have prerequisite knowledge that the student has not completed
    public static List<String> getCoursesWithUnmetPrerequisites(String studentNo, String[] selectedCourses) {
        List<String> unmetCourses = new ArrayList<>();

        //database data for courses with prerequisites and a list of prerequisites
        try (Connection conn = datasource.getConnection()) {
            for (String courseID : selectedCourses) {
                String prereqQuery = "SELECT preReqKnowledge FROM preRequisiteKnowledge WHERE courseID = ?";
                PreparedStatement prereqStmt = conn.prepareStatement(prereqQuery);
                prereqStmt.setString(1, courseID);
                ResultSet rs = prereqStmt.executeQuery();
    
                while (rs.next()) {
                    String requiredID = rs.getString("preReqKnowledge");
                    //database data for courses with prerequisites and student registration information to check if a student has compleated those classes
                    String checkCompletion = "SELECT COUNT(*) FROM StudentCourseRegistration " +
                            "WHERE stdNo = ? AND courseID = ? AND grade IN ('P', 'C', 'D', 'HD')";
                    PreparedStatement compStmt = conn.prepareStatement(checkCompletion);
                    compStmt.setString(1, studentNo);
                    compStmt.setString(2, requiredID);
                    ResultSet checkRs = compStmt.executeQuery();
    
                    if (checkRs.next() && checkRs.getInt(1) == 0 && !unmetCourses.contains(courseID)) {
                        unmetCourses.add(courseID);
                        break;
                    }
                    compStmt.close();
                }
                prereqStmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unmetCourses;
    }

    //register the student
    public static boolean registerStudent(String studentNo, String courseID, int semesterID) {
        try (Connection conn = datasource.getConnection()) {
            String sql = "INSERT INTO StudentCourseRegistration (stdNo, courseID, semesterID, grade, mark) VALUES (?, ?, ?, NULL, NULL)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            //put course information into StudentCourseRegistration
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
