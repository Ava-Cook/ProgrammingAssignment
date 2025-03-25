package seng2050;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EnrollServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] selectedCourses = request.getParameterValues("courses");
        HttpSession session = request.getSession();
        session.setAttribute("selectedCourses", selectedCourses);
        response.sendRedirect("confirmation.jsp");
    }
}
