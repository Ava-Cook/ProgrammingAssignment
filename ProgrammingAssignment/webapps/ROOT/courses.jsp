<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="seng2050.Course" %>
<%@ page import="seng2050.CourseBean" %>

<html>
<head>
    <title>Course Offerings</title>
</head>
<body>
    <h2>Available Courses for Selected Semester</h2>

    <% 
        Integer semesterID = (Integer) session.getAttribute("selectedSemester");
        if (semesterID != null) { 
    %>
        <p><strong>Selected Semester ID:</strong> <%= semesterID %></p>
    <% 
        } else { 
    %>
        <p><strong>Error:</strong> No semester selected.</p>
    <% } %>

    <form action="EnrollServlet" method="post">
        <table border="1">
            <tr>
                <th>Select</th>
                <th>Course ID</th>
                <th>Course Name</th>
                <th>Credits</th>
            </tr>
            <%
                CourseBean courseBean = (CourseBean) request.getAttribute("courseBean");
                if (courseBean != null && !courseBean.getCourses().isEmpty()) {
                    for (Course course : courseBean.getCourses()) {
            %>
            <tr>
                <td><input type="checkbox" name="selectedCourses" value="<%= course.getCourseID() %>"></td>
                <td><%= course.getCourseID() %></td>
                <td><%= course.getCourseName() %></td>
                <td><%= course.getCredits() %></td>
            </tr>
            <% 
                    }
                } else { 
            %>
            <tr>
                <td colspan="4">No courses available for this semester.</td>
            </tr>
            <% } %>
        </table>
        <input type="submit" value="Enroll">
    </form>
</body>
</html>
