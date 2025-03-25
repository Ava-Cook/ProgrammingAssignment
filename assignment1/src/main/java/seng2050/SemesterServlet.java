package seng2050;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//@WebServlet("/SemesterServlet")
public class SemesterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String selectedSemester = request.getParameter("semester");
        
        HttpSession session = request.getSession();
        session.setAttribute("semester", selectedSemester);
        
        response.sendRedirect("courses.jsp"); // Redirect to course selection page
    }
}
    

