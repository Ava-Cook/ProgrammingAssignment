<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="seng2050.Course" %>
<jsp:include page="header.jspf" />
<html>
<head>
    <title>Registration Confirmation</title>
    <link rel="stylesheet" type="text/css" href="confirmation.css">
</head>
<body>
    <h2>Registration Confirmation</h2>
    <% 
        Integer semesterID = (Integer) session.getAttribute("selectedSemester");
        String studentNo = (String) session.getAttribute("studentNo");
        List<Course> registeredCourses = (List<Course>) request.getAttribute("registeredCourses");
        String message = (String) request.getAttribute("message");
    %>


    <p><strong>Student ID:</strong> <%= studentNo %></p>
    <p><strong>Semester ID:</strong> <%= semesterID %></p>

    <h3>Registered Courses:</h3>
    <table border="1">
        <tr>
            <th>Course ID</th>
            <th>Course Name</th>
            <th>Credits</th>
        </tr>
        <% if (registeredCourses != null && !registeredCourses.isEmpty()) {
            for (Course course : registeredCourses) { %>
        <tr>
            <td><%= course.getCourseID() %></td>
            <td><%= course.getCourseName() %></td>
            <td><%= course.getCredits() %></td>
        </tr>
        <% }
        } else { %>
        <tr>
            <td colspan="3">No courses registered.</td>
        </tr>
        <% } %>
    </table>
    
</body>
</html>
