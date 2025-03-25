package seng2050;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import seng2050.SemesterBean;

public class LoginServlet extends HttpServlet {
   
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String username = request.getParameter("studentNo");
        String password = request.getParameter("password");

        StudentService stdServervice = new StudentService();
        Student student = stdServervice.authenticateStudent(username, password);

        if(student!= null){
            HttpSession session = request.getSession();
            request.setAttribute("student", student);

            SemesterBean semesterBean = new SemesterBean();
            semesterBean.setSemesterList(new String[]{"Fall 2025", "Spring 2026"});
            session.setAttribute("semesterBean", semesterBean);

            RequestDispatcher dispatcher = request.getRequestDispatcher("semester.jsp");
            dispatcher.forward(request, response);
        }
        /*  Hardcoded credentials (Replace with database validation in future)
        if ("student".equals(username) && "password123".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            // Create SemesterBean object
            SemesterBean semesterBean = new SemesterBean();
            semesterBean.setSemesterList(new String[]{"Fall 2025", "Spring 2026"});
            session.setAttribute("semesterBean", semesterBean);
            response.sendRedirect("semester.jsp");
        } */
        else {
            response.sendRedirect("login.jsp?error=Invalid username or password");
        }
    }
}
