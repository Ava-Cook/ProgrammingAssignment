<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - UniX Course Enrolment</title>
</head>
<body>
    <h2>Login</h2>
    <% 
        String error = request.getParameter("error");
        if (error != null) { 
    %>
        <p style="color:red;">Invalid student number or password. Please try again.</p>
    <% } %>
    
    <form action="LoginServlet" method="post">
        StudentNo: <input type="text" name="username" required><br>
        Password: <input type="password" name="password" required><br>
        <input type="submit" value="Login">
    </form>
</body>
</html>
