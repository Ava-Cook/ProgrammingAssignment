package seng2050;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/EnrollServlet")
public class EnrollServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer semesterID = (Integer) session.getAttribute("selectedSemester");
        String studentNo = (String) session.getAttribute("studentNo");
        
        if (semesterID == null) {
            
            response.sendRedirect("semester.jsp");
            return;
        }

        if (studentNo != null && semesterID != null) {
            int currentCredits = RegisterDAO.getTotalCredits(studentNo, semesterID);
            request.setAttribute("currentCredits", currentCredits);
        }

        List<Course> courses = CourseDAO.getCoursesBySemester(semesterID);
        CourseBean courseBean = new CourseBean(courses);
        request.setAttribute("courseBean", courseBean);
        
        Map<String, Boolean> fullCourses = RegisterDAO.getFullCoursesForSemester(semesterID);
        request.setAttribute("fullCourses", fullCourses);
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
        String[] selectedCourses = request.getParameterValues("selectedCourses");   
       
        //Check for unmet prereqs 
        List<String> unmetPreReqCourses = RegisterDAO.getCoursesWithUnmetPrerequisites(studentNo, selectedCourses);
        if (!unmetPreReqCourses.isEmpty()) {
            request.setAttribute("unmetPreReqCourses", unmetPreReqCourses);
            request.setAttribute("selectedCourses", selectedCourses);
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        // Check for unmet assumed knowledge
        List<String> unmetCourses = RegisterDAO.getCoursesWithUnmetAssumedKnowledge(studentNo, selectedCourses);
        if (!unmetCourses.isEmpty() && request.getParameter("proceedAnyway") == null) {
            request.setAttribute("unmetCourses", unmetCourses);
            request.setAttribute("selectedCourses", selectedCourses);
            request.getRequestDispatcher("warning.jsp").forward(request, response);
            return;
        }
        
        //register for courses
        if (selectedCourses != null) {
            for (String courseID : selectedCourses) {
                boolean success = RegisterDAO.registerStudent(studentNo, courseID, semesterID);
                if (!success) {
                    System.out.println("Failed to register student " + studentNo + " for course " + courseID);
                }
            }
        }
        
        // Retrieve registered courses for confirmation page
        List<Course> registeredCourses = RegisterDAO.getRegisteredCourses(studentNo, semesterID);
        request.setAttribute("registeredCourses", registeredCourses);
        // Forward to confirmation page
        request.getRequestDispatcher("confirmation.jsp").forward(request, response);
    }
}
