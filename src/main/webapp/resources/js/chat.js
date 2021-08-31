$(document).off('.data-api')
let currentFirstMessageId = 2147483647;
let chatId;
function connect() {
    let socket = new SockJS('/123');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/queue/chat/' + chatId, function (messageText) {
            console.log("getting post");
            let postObj = JSON.parse(messageText.body);
            showPost(postObj);
        });
    });
}

function showPost(messageText) {
    let div = document.getElementById('chat-list');
    let innerHTML = '';
    innerHTML +=
        '<p >\n' +
        '   <div class="d-flex w-100 align-items-center justify-content-between">\n' +
        '       <strong class="mb-1">' + messageText.name + '</strong>\n' +
        '   </div>\n' +
        '   <div class="col-10 mb-1 small">' + messageText.text + '</div>\n' +
        '</p>'
    let innerDiv = document.createElement('div');
    innerDiv.innerHTML = innerHTML;
    div.insertAdjacentElement("afterbegin", innerDiv);
}

function send() {
    const message = {
        text: $("#text").val(),
        chatId: chatId,
        name: $("#name").val(),
    };
    stompClient.send("/app/chat", {}, JSON.stringify(message));
}

$(function () {
//    $(document).ready(get);
    chatId = $("#chatId").val();
//    $(window).scroll(function () {
//        if($('#chat-list').height() + $('#chat-list').scrollTop() + 40 >= $('#chat-list').height() && !block) {
//            block = true;
//            get();
//        }
//    });
    connect();
    // $('#chat-list').scrollTop($('#chat-list').prop("scrollHeight"));
    $( "#send" ).click(function() { send(); });

    function get() {
        let data = {firstMessageID: currentFirstMessageId};
        $.get("chat/get-messages", data, success, "json");
    }

    function success(messageText) {
        let div = document.getElementById('chat-list');
        let innerHTML = '';
        for (let i = 0; i < messageText.length; i++) {
            innerHTML +=
                '<p>\n' +
                '   <div class="d-flex w-100 align-items-center justify-content-between">\n' +
                '       <strong class="mb-1">' + messageText[i].name + '</strong>\n' +
                '   </div>\n' +
                '   <div class="col-10 mb-1 small">' + messageText[i].text + '</div>\n' +
                '</p>'
        }
        let innerDiv = document.createElement('div');
        innerDiv.innerHTML = innerHTML;
        div.insertAdjacentElement("afterbegin", innerDiv);
        currentFirstMessageId = messageText[messageText.length-1].id;
        block = false;
    }

});