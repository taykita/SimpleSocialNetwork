
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
<%@ page import="mainLogic.WelcomeLogic"%>
<html>
<head>
    <title>Time!</title>
</head>
<body>

    <%
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");

        String welcomeString = new WelcomeLogic().getWelcomeString(name, surname);
    %>

    <h2> <%= welcomeString + "!" %> </h2>

    <p> <%= "Текущее время - " + new Date() %> </p>

</body>
</html>