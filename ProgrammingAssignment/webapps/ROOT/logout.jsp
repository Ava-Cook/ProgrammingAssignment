<%@ page language="java" session="true" %>
<%
    session.invalidate(); // Destroys the session
    response.sendRedirect("login.jsp"); // Redirect to login
%>