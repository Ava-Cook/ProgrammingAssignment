<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="seng2050.CourseBean" %> 
<!DOCTYPE html>
<html>
<head>
    <title>Course Selection</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h2>Select Your Courses</h2>
    <% CourseBean courseBean = new CourseBean(); %>
    <form action="EnrollServlet" method="post">
        <% for (String course : courseBean.getCourseList()) { %>
            <input type="checkbox" name="courses" value="<%= course %>"> <%= course %><br>
        <% } %>
        <input type="submit" value="Enroll">
    </form>
</body>
</html>