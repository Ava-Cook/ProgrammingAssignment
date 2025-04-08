<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Assumed Knowledge Warning</title>
    <link rel="stylesheet" type="text/css" href="warning.css">
</head>
<body>
    <h2>Warning</h2>
    <p>The following courses require assumed knowledge that you have not completed:</p>
    <ul>
        <%
            String[] selectedCourses = (String[]) request.getAttribute("selectedCourses");
            List<String> unmetCourses = (List<String>) request.getAttribute("unmetCourses");
            for (String course : unmetCourses) {
        %>
            <li><%= course %></li>
        <% } %>
    </ul>
    <p>Would you like to proceed with enrollment anyway?</p>

    <form method="post" action="EnrollServlet">
        <% for (String course : selectedCourses) { %>
            <input type="hidden" name="selectedCourses" value="<%= course %>">
        <% } %>
        <input type="hidden" name="proceedAnyway" value="true">
        <button type="submit">Proceed Anyway</button>
        <button type="button" onclick="history.back();">Cancel</button>
    </form>
</body>
</html>
