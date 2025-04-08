package seng2050;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SemesterServlet")
public class SemesterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Get the selected semester from the form
        String semesterIDStr = request.getParameter("semester");
        
        if (semesterIDStr != null) {
            try {
                int semesterID = Integer.parseInt(semesterIDStr);
                session.setAttribute("selectedSemester", semesterID);

                // Redirect to the course selection page
                response.sendRedirect("EnrollServlet");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("semester.jsp?error=Invalid semester");
            }
        } else {
            response.sendRedirect("semester.jsp?error=No semester selected");
        }
    }
}

    

