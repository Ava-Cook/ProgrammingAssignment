package seng2050;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SemesterDAO {
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

    public List<Semester> getAllSemesters() {
        List<Semester> semesters = new ArrayList<>();
        String sql = "SELECT * FROM Semester";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("semesterID");
                int semester = rs.getInt("semester");
                int year = rs.getInt("year");
                semesters.add(new Semester(id, semester, year));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return semesters;
    }
}
