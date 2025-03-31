package seng2050;


import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

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

    @Override
    public void addStudent(Student student) {
        String sql = "INSERT INTO student (stdNO, givenNames, lastName, passwordHash) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getStdNo());
            stmt.setString(2, student.getGivenNames());
            stmt.setString(3, student.getLastName());
            stmt.setString(3, student.getPasswordHash());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudentByStdNo(String stdNo) {
        String sql = "SELECT * FROM student WHERE stdNo = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stdNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(rs.getString("stdNo"), rs.getString("givenNames"), 
                    rs.getString("lastName"), rs.getString("passwordHash"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                students.add(new Student(rs.getString("stdNo"), rs.getString("givenNames"), 
                        rs.getString("lastName"), rs.getString("passwordHash")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }


    @Override
    public void updateStudent(Student student) {
        String sql = "UPDATE student SET givenNames = ?, lastName = ? WHERE stdNo = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getGivenNames());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getStdNo());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStudent(String stdNo) {
        String sql = "DELETE FROM student WHERE stdNo = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, stdNo);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



