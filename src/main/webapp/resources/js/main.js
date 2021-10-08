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
            items: []
        };
    }

    componentDidMount() {
        fetch("main/get-active-menu-button")
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
        const { error, isLoaded, active } = this.state;
        if (error) {
            return <div>Ошибка: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Загрузка...</div>;
        } else {
            return (
                <div className="col-md-3">
                    <div className="position-sticky">
                        <div className="p-2 mb-3">
                            <div className="btn-group-vertical btn-group-toggle" data-toggle="buttons">
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
            items: []
        };
    }

    componentDidMount() {
        fetch("main/get-user-name")
            .then(res => res.text())
            .then(
                (result) => {
                    this.setState({
                        isLoaded: true,
                        name: result
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
        const { error, isLoaded, name } = this.state;
        if (error) {
            return <div>Ошибка: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Загрузка...</div>;
        } else {
            return (
                <div className="col-md-10">
                    <h1>{name}</h1>
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

function CreatePost() {
    return (
        <div className="row col-md-7">
            <form action="create-post" method="POST">
                <div className="form-floating">
                    <input type="text" className="form-control" name="postText" id="postText" />
                        <label htmlFor="postText">Введите текст</label>
                </div>

                <button className="w-100 btn btn-lg btn-dark" type="submit" id="send">Опубликовать</button>
            </form>
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
        let div = document.getElementById('post');
        let innerHTML = '';
        for (let i = 0; i < posts.length; i++) {
            innerHTML +=
                '<div class="row">\n' +
                '   <div class="col-md-7">\n' +
                '       <div class="p-2">\n' +
                '           <div class="post">\n' +
                '               <p class="list">' + posts[i].date + '</p>\n' +
                '               <p class="list">' + posts[i].text + '</p>\n' +
                '           </div>\n' +
                '       </div>\n' +
                '   </div>\n' +
                '   <div class="col-md-2">\n' +
                '       <div class="pt-2 pb-1">\n' +
                '            <div class="pt-1 pb-1">\n' +
                '                <div class="delete-post">\n' +
                '                    <form action="delete-post" method="post">\n' +
                '                        <input type="text" name="id" value="' + posts[i].id + '" hidden/>\n' +
                '                        <button class="w-100 btn btn-lg btn-secondary" type="submit">Удалить</button>\n' +
                '                    </form>\n' +
                '                </div>\n' +
                '           </div>\n' +
                '           <div class="pt-1 pb-1">\n' +
                '                <div class="edit-post">\n' +
                '                    <form action="edit-post-page" method="post">\n' +
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
        currentFirstPostId = posts[posts.length-1].id;
        block = false;
    }

});