package seng2050;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/AssumedKnowledgeServlet")
public class AssumedKnowledgeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String studentNo = (String) session.getAttribute("studentNo");
        String[] selectedCourses = request.getParameterValues("selectedCourses");

        if (studentNo == null || selectedCourses == null) {
            response.sendRedirect("semester.jsp");
            return;
        }

        List<String> unmetCourses = RegisterDAO.getCoursesWithUnmetAssumedKnowledge(studentNo, selectedCourses);

        if (!unmetCourses.isEmpty()) {
            request.setAttribute("unmetCourses", unmetCourses);
            request.setAttribute("selectedCourses", selectedCourses);
            request.getRequestDispatcher("warning.jsp").forward(request, response);
        } else {
            // No unmet knowledge – redirect to EnrollServlet
            request.setAttribute("selectedCourses", selectedCourses);
            request.getRequestDispatcher("EnrollServlet").forward(request, response);
        }
    }
}

