<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Redirect</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
                  integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    </head>

    <body>
        <%
            String path = request.getScheme() + "://" + request.getServerName() + request.getContextPath() + "/user-page?id=" + request.getParameter("id");
            response.sendRedirect(path);
        %>
    </body>

</html>