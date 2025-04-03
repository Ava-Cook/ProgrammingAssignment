package seng2050;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/EnrollServlet")
public class EnrollServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer semesterID = (Integer) session.getAttribute("selectedSemester");
       
        
        if (semesterID == null) {
            
            response.sendRedirect("semester.jsp");
            return;
        }
        
        List<Course> courses = CourseDAO.getCoursesBySemester(semesterID);
        CourseBean courseBean = new CourseBean(courses);
        request.setAttribute("courseBean", courseBean);

        request.getRequestDispatcher("courses.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer semesterID = (Integer) session.getAttribute("selectedSemester");
        String studentNo = (String) session.getAttribute("studentNo");  

        if (semesterID == null || studentNo == null) {
            response.sendRedirect("semester.jsp?error=Invalid session");
            return;
        }

        // Get selected course IDs from the form
        String[] selectedCourses = request.getParameterValues("selectedCourses");// Store messages to show on confirmation page
        
        String message = null;
        boolean success = true; 
    
        if (selectedCourses != null) {
            for (String courseID : selectedCourses) {
                try {
                    boolean registered = RegisterDAO.registerStudent(studentNo, courseID, semesterID);
                    if (!registered) {
                        message = "Failed to register for course: " + courseID;
                        success = false;
                    }
                } catch (Exception e) {
                    message = e.getMessage(); // Capture the specific error message
                    success = false;
                    break; // Stop further registrations if an error occurs
                }
            }
        }
    
        if (success && message == null) {
            message = "Registration successful!";
        }
    
        // Retrieve registered courses for confirmation page
        List<Course> registeredCourses = RegisterDAO.getRegisteredCourses(studentNo, semesterID);
        request.setAttribute("message", message);
        request.setAttribute("registeredCourses", registeredCourses);

        // Forward to confirmation page
        request.getRequestDispatcher("confirmation.jsp").forward(request, response);
    }
}
