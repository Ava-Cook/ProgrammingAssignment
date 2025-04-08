<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Pre-requisite Error</title>
    <link rel="stylesheet" type="text/css" href="error.css">
</head>
<body>
    <h2>Error</h2>
    <p>You cannot enroll in the following courses because you have not completed the required pre-requisites:</p>
    <ul>
        <%
            String[] selectedCourses = (String[]) request.getAttribute("selectedCourses");
            List<String> unmetPreReqCourses = (List<String>) request.getAttribute("unmetPreReqCourses");
            for (String course : unmetPreReqCourses) {
        %>
            <li><%= course %></li>
        <% } %>
    </ul>
    <form method="get" action="EnrollServlet">
        <button type="submit">Return to Course Selection</button>
    </form>
</body>
</html>
