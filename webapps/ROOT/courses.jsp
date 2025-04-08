<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="seng2050.Course" %>
<%@ page import="seng2050.CourseBean" %>
<%@ page import="java.util.Map" %>
<%@ page import="seng2050.RegisterDAO" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<jsp:include page="header.jspf" />
<html>
<head>
    <title>Course Offerings</title>
    <link rel="stylesheet" type="text/css" href="courses.css">
    <script src="courses.js" defer></script>
</head>
<body>
    <h2>Available Courses for Selected Semester</h2>

    <% 
        Integer semesterID = (Integer) session.getAttribute("selectedSemester");
        Integer currentCreditsObj = (Integer) request.getAttribute("currentCredits");
        int currentCredits = currentCreditsObj != null ? currentCreditsObj : 0;
        boolean isAtCreditLimit = currentCredits >= 40;
        String studentNo = (String) session.getAttribute("studentNo"); // Assume student's studentNo is stored in session
        List<Course> enrolledCourses = RegisterDAO.getRegisteredCourses(studentNo, semesterID); // Fetch enrolled courses

        // Create a set of enrolled course IDs for quick lookup
        Set<String> enrolledCourseIDs = new HashSet<>();
        for (Course course : enrolledCourses) {
            enrolledCourseIDs.add(course.getCourseID());
        }
        if (semesterID != null) { 
    %>
    <p>
        <strong>Selected Semester ID:</strong> <%= semesterID %>
        <div class="change-semester-btn">
        <form action="semester.jsp">
            <button type="submit">Change Semester</button>
        </form>
        </div>
        
    </p>
        <p><strong>Credits Enrolled:</strong> <span id="currentCredits"><%= currentCredits %></span>/40</p>
    <% 
        } else { 
    %>
        <p><strong>Error:</strong> No semester selected.</p>
    <% } %>

    <%
        Map<String, Boolean> fullCourses = (Map<String, Boolean>) request.getAttribute("fullCourses");
        CourseBean courseBean = (CourseBean) request.getAttribute("courseBean");
        if (courseBean != null && !courseBean.getCourses().isEmpty()) {
    %>

    <form action="EnrollServlet" method="post">
        <table border="1">
            <tr>
                <th>Course ID</th>
                <th>Course Name</th>
                <th>Credits</th>
                <th>Action</th>
            </tr>

            <% for (Course course : courseBean.getCourses()) {
                   boolean isFull = fullCourses != null && fullCourses.getOrDefault(course.getCourseID(), false);
                   boolean isEnrolled = enrolledCourseIDs.contains(course.getCourseID());
            %>
            <tr>
                <td><%= course.getCourseID() %></td>
                <td><%= course.getCourseName() %></td>
                <td><%= course.getCredits() %></td>
                <td>
                    <% if (isFull) { %>
                        <button type="button" class="enroll-btn disabled" disabled title="Course is full">Full</button>
                    <% } else if (isAtCreditLimit) { %>
                        <button type="button" class="enroll-btn disabled" disabled title="Credit limit reached">Maxed</button>
                    <% }else if (isEnrolled) { %>
                        <button type="button" class="enroll-btn disabled" disabled title="Already enrolled">Enrolled</button> 
                    <% } else { %>
                    
                        <button 
                            type="button" 
                            class="enroll-btn <%= isFull ? "disabled" : "" %>" 
                            data-courseid="<%= course.getCourseID() %>" 
                            data-credits="<%= course.getCredits() %>"
                            <%= isFull ? "disabled title='Course is full'" : "" %>>
                            <%= isFull ? "Full" : "Enroll" %>
                        </button>

                        <input 
                            type="checkbox" 
                            name="selectedCourses" 
                            value="<%= course.getCourseID() %>" 
                            id="input-<%= course.getCourseID() %>" 
                            style="display:none;">
                    <% } %>
                </td>
            </tr>
            <% } %>
        </table>
        <br>
        <input type="submit" value="Confirm" class="submit-btn">
    </form>

    <% } else { %>
        <p>No courses available for this semester.</p>
    <% } %>
</body>
</html>
