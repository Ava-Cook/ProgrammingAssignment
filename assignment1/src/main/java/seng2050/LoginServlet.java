package seng2050;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");

        // Retrieve input parameters
        String username = request.getParameter("studentNo");
        String password = request.getParameter("password");

        
        StudentService stdServervice = new StudentService();
        Student student = stdServervice.authenticateStudent(username, password);
        if (student!= null) {
            // Store stduent in request scope
            request.setAttribute("student", student);

            SemesterDAO semesterDAO = new SemesterDAO();
            List<Semester> semesterList = semesterDAO.getAllSemesters();

            HttpSession session = request.getSession();
            session.setAttribute("student", student);
            session.setAttribute("semesterList", semesterList);
            session.setAttribute("studentNo", username);
            // Forward to Welcome.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("semester.jsp");
            dispatcher.forward(request, response);
        }
        
        else {
            response.sendRedirect("login.jsp?error=Invalid username or password");
        }
    }
}
