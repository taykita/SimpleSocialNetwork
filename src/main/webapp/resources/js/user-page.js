let currentFirstPostId = 2147483647;
$(document).off('.data-api')

let url = new URL(window.location.href);
let searchParams = new URLSearchParams(url.search.substring(1));
let currentId = searchParams.get("id");

let block = false;

function connect() {
    let socket = new SockJS('/socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/queue/user-page/' + currentId, function (post) {
            console.log("getting post");
            let postObj = JSON.parse(post.body);
            showPost(postObj);
        });
    });
}

function defaultImg() {
    document.getElementById('avatar').src = 'resources/images/avatars/1.png';
}

function showPost(post) {
    let div = document.getElementById('post');
    let innerHTML = '';
    innerHTML +=
        '<div class="row p-2">\n' +
        '   <div class="col-md-7 shadow">\n' +
        '       <div class="p-2">\n' +
        '           <div class="post">\n' +
        '               <p class="list">' + post.date + '</p>\n' +
        '               <p class="list">' + post.text + '</p>\n' +
        '           </div>\n' +
        '       </div>\n' +
        '   </div>\n' +
        '</div>\n';
    let innerDiv = document.createElement('div');
    innerDiv.innerHTML = innerHTML;
    div.insertAdjacentElement("afterbegin", innerDiv);
}

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
        let data = {firstPostId: currentFirstPostId, id: currentId};
        $.get("user-page/get-posts", data, showPostList, "json");
    }

    function showPostList(posts) {
        let div = document.getElementById('post');
        let innerHTML = '';
        for (let i = 0; i < posts.length; i++) {
            innerHTML +=
                '<div class="row p-2">\n' +
                '   <div class="col-md-7 shadow">\n' +
                '       <div class="p-2">\n' +
                '           <div class="post">\n' +
                '               <p class="list">' + posts[i].date + '</p>\n' +
                '               <p class="list">' + posts[i].text + '</p>\n' +
                '           </div>\n' +
                '       </div>\n' +
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
