<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="A.K.">
        <meta name="generator" content="Hugo 0.84.0">
        <title>Регистрация</title>

        <link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/sign-in/">



        <!-- CSS only -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
            integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <style>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }
        </style>


        <!-- Custom styles for this template -->
        <link href="cssFiles/signIn.css" rel="stylesheet">
    </head>
    <body class="text-center">
        <main class="form-signin">
            <form action="http://booknetwork.site/myapp/create-acc">
                <h1 class="h3 mb-3 fw-normal">Вход</h1>

                <div class="form-floating">
                    <input type="email" class="form-control" name="email" id="email">
                    <label for="email">Адрес почты</label>
                </div>
                <div class="form-floating">
                    <input type="password" class="form-control" name="pass" id="pass">
                    <label for="pass">Пароль</label>
                </div>
                <div class="form-floating">
                    <input type="password" class="form-control" name="chPass" id="chPass">
                    <label for="chPass">Подтвердите пароль</label>
                </div>

                <button class="w-100 btn btn-lg btn-dark" type="submit">Войти</button>

                <div class="mt-3 mb-3 text-body">
                    <p>Уже есть аккаунт? <a href="index.jsp"> Войдите </a> </p>
                </div>


                <p class="mt-5 mb-3 text-muted">&copy; 2021–2021</p>
            </form>
        </main>
    </body>
</html>