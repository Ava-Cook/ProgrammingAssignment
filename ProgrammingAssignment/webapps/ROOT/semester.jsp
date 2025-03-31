<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="seng2050.Semester, java.util.List" %> 
<!DOCTYPE html>
<html>
<head>
    <title>Select Semester</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h2>Select Your Semester</h2>

    <% 
        List<Semester> semesterList = (List<Semester>) session.getAttribute("semesterList");
        seng2050.Student student = (seng2050.Student) session.getAttribute("student");
    %>

    <% if (semesterList != null && student != null) { %>
        <p>Welcome ${student.givenNames}! Please select a semester:</p>
        <form action="SemesterServlet" method="post">
            <label for="semester">Select a semester:</label>
            <select name="semester" id="semester" required>
                <% for (Semester semester : semesterList) { %>
                    <option value="<%= semester.getSemesterID() %>">
                        Semester <%= semester.getSemester() %> - <%= semester.getYear() %>
                    </option>
                <% } %>
            </select>
            <br><br>
            <input type="submit" value="Submit">
        </form>        
    <% } else { %>
        <p>Error: Semester information is missing.</p>
    <% } %>
</body>
</html>


