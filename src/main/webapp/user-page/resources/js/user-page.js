let currentFirstPostId = 2147483647;
$(document).off('.data-api')

let url = window.location.href;
let currentId = url.split('/').pop();

let block = false;

function connect() {
    let socket = new SockJS('/123');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user-page/' + currentId, function (post) {
            console.log("getting post");
            let postObj = JSON.parse(post.body);
            showPost(postObj);
        });
    });
}

function showPost(post) {
    let div = document.getElementById('post');
    let innerHTML = '';
    innerHTML +=
        '<div class="row">\n' +
        '   <div class="col-md-7">\n' +
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
        $.get("user-page/get-posts", data, success, "json");
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
                '</div>\n';
        }
        let innerDiv = document.createElement('div');
        innerDiv.innerHTML = innerHTML;
        div.insertAdjacentElement("beforeend", innerDiv);
        currentFirstPostId = posts[posts.length-1].id;
        block = false;
    }
});
