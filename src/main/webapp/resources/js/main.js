let stompClient = null;

let block = false;

function connect() {
    let socket = new SockJS('/socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
    });
}

let currentFirstPostId = 2147483647;

function Root() {
    return (
        <div class="row">
            <SideMenu active={getActiveMenuB()}/>
            <div class="col-md-9">
                <div class="row">
                    <Name name={getUserName()}/>
                    <ExitB/>
                </div>
                <div className="row">
                    <Avatar/>
                    <Info/>
                </div>
                <h1>Посты</h1>
                <div id="post">

                </div>
                <LoadMoreB/>
            </div>
        </div>

    );
}

function getActiveMenuB() {
    let active;
    $.ajax({url:"main/get-active-menu-button", async:false, success:setActive, dataType:"json"});

    function setActive(data) {
        active = data;
    }

    return active;
}

function getUserName() {
    let userName;
    $.ajax({url:"main/get-user-name", async:false, success:setUserName, dataType:"text"});

    function setUserName(data) {
        console.log(data);
        userName = data;
    }

    console.log(userName);

    return userName;
}

function SideMenu(props) {
    return (
        <div className="col-md-3">
            <div className="position-sticky">
                <div className="p-2 mb-3">
                    <div className="btn-group-vertical btn-group-toggle" data-toggle="buttons">
                        <a className={checkActive(props.active, "MAIN")} href="main">
                            Моя страница
                        </a>
                        <a className={checkActive(props.active, "CHAT")} href="chat-list">
                            Сообщения
                        </a>
                        <a className={checkActive(props.active, "NEWS")} href="news">
                            Новости
                        </a>
                        <a className={checkActive(props.active, "FRIENDS")} href="friend-list">
                            Друзья
                        </a>
                        <a className={checkActive(props.active, "USERS")} href="user-list">
                            Список пользователей
                        </a>
                    </div>
                </div>
            </div>
        </div>
    );
}

function checkActive(active, menuItemName) {
    if (active.name === menuItemName) {
        return "btn btn-md btn-secondary active";
    } else {
        return "btn btn-md btn-secondary";
    }
}

function Name(props) {
    return (
        <div className="col-md-10">
            <h1>{props.name}</h1>
        </div>
    );
}

function ExitB() {
    return (
        <div className="col-md-2 exitB">
            <a className="w-30 btn btn-lg btn-secondary" href="exit" type="submit">Выход</a>
        </div>
    );
}

function Avatar() {
    return (
        <div className="col-md-3">
            <img src="main/load/avatar" width="200" height="200"
                 alt="Не удалось загрузить картинку"/>
                <form method="post" action="main/upload" encType="multipart/form-data">
                    <input type="file" name="image"/>
                    <button className="w-30 btn btn-lg btn-secondary" value="Upload" type="submit">Загрузить фото
                    </button>
                </form>
        </div>
    );
}

function Info() {
    return (
        <div className="col-md-9">
            <p>Информация</p>
            <div className="col-md-2 p-2 exitB">
                <a className="w-30 btn btn-lg btn-secondary" href="edit-acc" type="submit">Редактировать</a>
            </div>
        </div>
    )
}

function Posts(props) {
    return (
         <div class="row">
            <div class="col-md-7">
                <div class="p-2">
                    <div class="post">
                        <p class="list">{props.date}</p>
                        <p class="list">{props.text}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-2">
                <div class="pt-2 pb-1">
                     <div class="pt-1 pb-1">
                         <div class="delete-post">
                             <form action="delete-post" method="post">
                                 <input type="text" name="id" value={props.id} hidden/>
                                 <button class="w-100 btn btn-lg btn-secondary" type="submit">Удалить</button>
                             </form>
                         </div>
                    </div>
                    <div class="pt-1 pb-1">
                         <div class="edit-post">
                             <form action="edit-post-page" method="post">
                                <input type="text" name="id" value={props.id} hidden/>
                                <button class="w-100 btn btn-lg btn-secondary" type="submit">Редактировать</button>
                             </form>
                         </div>
                    </div>
               </div>
            </div>
         </div>
    );
}

function LoadMoreB() {
    return (
        <div className="row">
            <div className="col-md-2">
                <div className="pt-2 pb-1">
                    <div className="pt-1 pb-1">
                        <button className="w-100 btn btn-lg btn-secondary" type="submit" id="load-post">Загрузить ещё
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

$(document).off('.data-api')
$(function () {
    ReactDOM.render(
        <Root />,
        document.getElementById('root')
    );

    $(document).ready(get);
    $('#load-post').click(get);

    let files;

    $('input[type=file]').on('change', function(){
        files = this.files;
    });

    $(window).scroll(function () {
        if($(window).height() + $(window).scrollTop() + 40 >= $(document).height() && !block) {
            block = true;
            get();
        }
    });

    connect();

    function get() {
        let data = {firstPostId: currentFirstPostId};
        $.get("main/get-posts", data, showPostList, "json");
    }

    function showPostList(posts) {
        let element = document.getElementById('post');

        for (let i = 0; i < posts.length; i++) {
            ReactDOM.render(
                <Posts date={posts[i].date} name={posts[i].name} id={posts[i].id} />,
                element
            );
        }

        currentFirstPostId = posts[posts.length-1].id;
        block = false;
    }

});