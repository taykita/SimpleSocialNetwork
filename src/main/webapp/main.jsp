<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

    <link href="cssFiles/main.css" rel="stylesheet">
</head>
<body>
    <main class="BN-side-menu">
        <div class="row">
                <div class="col-md-3 ">
                    <div class="position-sticky">
                        <div class="p-2 mb-3">
                            <div class="btn-group-vertical btn-group-toggle " data-toggle="buttons">
                                <label class="btn btn-md btn-secondary active">
                                    Моя страница
                                </label>
                                <label class="btn btn-md btn-secondary">
                                    Сообщения
                                </label>
                                <label class="btn btn-md btn-secondary">
                                    Друзья
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-9">
                    <div class="row">
                        <div class="col-md-10">
                            <h1>Имя_пользователя</h1>
                        </div>
                        <div class="col-md-2">
                            <div class="exitB">
                                <button class="w-30 btn btn-lg btn-secondary" type="submit">Выход</button>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3">
                            <img src="defoult.png" width="200" height="200" alt="Не удалось загрузить картинку">
                        </div>
                        <div class="col-md-9">
                            <p>Информация</p>
                        </div>
                    </div>
                    <h1>Посты</h1>
                </div>
        </div>
    </main>
</body>
</html>