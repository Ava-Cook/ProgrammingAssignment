<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - UniX Course Enrolment</title>
</head>
<body>
    <h2>Enter Your Student Login</h2>
    <% 
        String error = request.getParameter("error");
        if (error != null) { 
    %>
        <p style="color:red;">Invalid student number or password. Please try again.</p>
    <% } %>
    <form action="login" method="POST">
        <p>
            <label for="student no">StudentNo:</label>
            <input type="username" name="studentNo" required>
        </p>
        <p>
          <label for="password">Password:</label>
          <input type="password" name="password" required>        
        </p>
        <button type="submit">Submit</button>
    </form>
</body>
</html>

