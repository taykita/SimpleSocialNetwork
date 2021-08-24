let stompClient = null;

let block = false;

function connect() {
    let socket = new SockJS('/123');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
    });
}

let currentFirstPostId = 2147483647;
$(document).off('.data-api')
$(function () {
    $(document).ready(get);
    $('#load-post').click(get);

    $(window).scroll(function () {
        if($(window).height() + $(window).scrollTop() + 40 >= $(document).height() && !block) {
                block = true;
                get();
            }
    });

    connect();

    function get() {
        let data = {firstPostId: currentFirstPostId};
        $.get("main/get-posts", data, success, "json");
    }

    function success(posts) {
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