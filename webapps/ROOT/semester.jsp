<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="seng2050.SemesterBean" %> 
<!DOCTYPE html>
<html>
<head>
    <title>Select Semester</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h2>Select Your Semester</h2>

    <% 
        SemesterBean semesterBean = (SemesterBean) session.getAttribute("semesterBean");
        String username = (String) session.getAttribute("username");
    %>

    <% if (semesterBean != null) { %>
        <p>Welcome, <%= username %>! Please select a semester:</p>
        <form action="SemesterServlet" method="post">
            <label for="semester">Select a semester:</label>
            <select name="semester" id="semester" required>
                <% for (String semester : semesterBean.getSemesterList()) { %>
                    <option value="<%= semester %>"><%= semester %></option>
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


