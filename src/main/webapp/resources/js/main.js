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
        <div className="row">
            <SideMenu/>
            <div className="col-md-9">
                <div className="row">
                    <Name/>
                    <ExitB/>
                </div>
                <div className="row">
                    <Avatar/>
                    <Info/>
                </div>
                <h1>Посты</h1>
                <CreatePost/>
                <div id="post">

                </div>
                <LoadMoreB/>
            </div>
        </div>

    );
}

class SideMenu extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            active: []
        };
    }

    componentDidMount() {
        fetch("/main/menu/bottoms/active")
            .then(res => res.json())
            .then(
                (result) => {
                    this.setState({
                        isLoaded: true,
                        active: result
                    });
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            )
    }

    render() {
        const {error, isLoaded, active} = this.state;
        if (error) {
            return <div>Ошибка: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Загрузка...</div>;
        } else {
            return (
                <div className="col-md-3">
                    <div className="position-sticky">
                        <div className="p-2 mb-3">
                            <div className="w-100 btn-group-vertical btn-group-toggle" data-toggle="buttons">
                                <a className={checkActive(active, "MAIN")} href="main">
                                    Моя страница
                                </a>
                                <a className={checkActive(active, "CHAT")} href="chat-list">
                                    Сообщения
                                </a>
                                <a className={checkActive(active, "NEWS")} href="news">
                                    Новости
                                </a>
                                <a className={checkActive(active, "FRIENDS")} href="friend-list">
                                    Друзья
                                </a>
                                <a className={checkActive(active, "USERS")} href="user-list">
                                    Список пользователей
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            );
        }
    }
}

class Name extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            user: []
        };
    }

    componentDidMount() {
        fetch("main/user")
            .then(res => res.json())
            .then(
                (result) => {
                    this.setState({
                        isLoaded: true,
                        user: result
                    });
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            )
    }

    render() {
        const {error, isLoaded, user} = this.state;
        console.log(user);
        if (error) {
            return <div>Ошибка: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Загрузка...</div>;
        } else {
            return (
                <div className="col-md-10">
                    <h1>{user.name}</h1>
                </div>
            );
        }
    }
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
            <img className="img" src="main/load/avatar" width="200" height="200"
                 alt="Не удалось загрузить картинку"/>
            <form method="post" action="main/upload" encType="multipart/form-data">
                <input className="w-75 form-control img-load" type="file" name="image"/>
                <button className="w-75 btn btn-lg btn-secondary img-load-btn" value="Upload" type="submit">Загрузить фото
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

function CreatePost() {
    return (
        <div className="row col-md-7">
            <div className="post-text form-floating">
                <input type="text" className="form-control" name="postText" id="postText"/>
                <label htmlFor="postText">Введите текст</label>
            </div>
            <div className="post-btn row col-md-12 pb-2">
                <button onClick={createPost} className="w-100 btn btn-lg btn-dark" id="send">Опубликовать</button>
            </div>
        </div>
    );
}

function createPost() {
    const data = {
        text: $("#postText").val(),
        id: 0
    };

    $.ajax({
        url:"posts",
        type:"POST",
        async: false,
        data: JSON.stringify(data),
        contentType:"application/json",
        dataType:"json",
        success: function (data) {
            window.location.href = "main";
        },
        error: function() {
            window.location.href = "main";
        }
    })
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
        <Root/>,
        document.getElementById('root')
    );

    $(document).ready(get);
    $('#load-post').click(get);

    let files;

    $('input[type=file]').on('change', function () {
        files = this.files;
    });

    $(window).scroll(function () {
        if ($(window).height() + $(window).scrollTop() + 40 >= $(document).height() && !block) {
            block = true;
            get();
        }
    });

    connect();

    function get() {
        let data = {firstPostId: currentFirstPostId};
        $.get("main/posts", data, showPostList, "json");
    }

    function showPostList(posts) {
        let div = document.getElementById('post');
        let innerHTML = '';
        for (let i = 0; i < posts.length; i++) {
            innerHTML +=
                '<div class="row">\n' +
                '   <div class="col-md-7">\n' +
                '       <div >\n' +
                '           <div class="post">\n' +
                '               <p class="list">' + posts[i].date + '</p>\n' +
                '               <p class="list">' + posts[i].text + '</p>\n' +
                '           </div>\n' +
                '       </div>\n' +
                '   </div>\n' +
                '   <div class="col-md-2">\n' +
                '       <div class="pt-2 pb-1">\n' +
                '           <div class="pt-1 pb-1">\n' +
                '                <div class="delete-post">\n' +
                '                    <button onclick="deletePost(' + posts[i].id + ')" class="w-100 btn btn-lg btn-secondary">Удалить</button>\n' +
                '                </div>\n' +
                '           </div>\n' +
                '           <div class="pt-1 pb-1">\n' +
                '                <div class="edit-post">\n' +
                '                    <form action="edit-post-page" method="get">\n' +
                '                       <input type="text" name="id" value="' + posts[i].id + '" hidden/>\n' +
                '                       <button class="w-100 btn btn-lg btn-secondary" type="submit">Редактировать</button>\n' +
                '                    </form>\n' +
                '                </div>\n' +
                '           </div>\n' +
                '      </div>\n' +
                '   </div>\n' +
                '</div>\n';
        }

        let innerDiv = document.createElement('div');
        innerDiv.innerHTML = innerHTML;
        div.insertAdjacentElement("beforeend", innerDiv);
        currentFirstPostId = posts[posts.length - 1].id;
        block = false;
    }



});

function deletePost(id) {
    $.ajax({
        url:"posts/" + id,
        type:"DELETE",
        success: function (data) {
            window.location.href = "main";
        },
        error: function() {
            window.location.href = "main";
        }

    })
}