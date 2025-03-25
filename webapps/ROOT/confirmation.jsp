<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Enrollment Confirmation</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h2>Enrollment Confirmation</h2>
    <p>You have successfully enrolled in the following courses:</p>
    <ul>
        <% String[] selectedCourses = (String[]) session.getAttribute("selectedCourses"); %>
        <% if (selectedCourses != null) { %>
            <% for (String course : selectedCourses) { %>
                <li><%= course %></li>
            <% } %>
        <% } else { %>
            <p>No courses selected.</p>
        <% } %>
    </ul>
</body>
</html>
