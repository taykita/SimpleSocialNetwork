$(document).off('.data-api')
let currentFirstMessageId = 2147483647;
let chatId;
let block = false;

function connect() {
    let socket = new SockJS('/socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/chat/' + chatId, function (messageText) {
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
        '   <div class="col-10 mb-1 small">' + messageText.date + '</div>\n' +
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
        name: $("#userName").val(),
        accId: $("#userId").val(),
    };
    stompClient.send("/app/chat", {}, JSON.stringify(message));
    $('#text').val('');
}

$(function () {
    $(document).ready(get);
    chatId = $("#chatId").val();
    connect();
    $(document).keypress(function (e) {
        if (e.keyCode === 13)
            send();
    });
    $("#load-message").click(function () {
        get();
    });
    $("#send").click(function () {
        send();
    });

    function get() {
        let data = {firstMessageId: currentFirstMessageId, chatId: chatId};
        $.get("chat/messages", data, showMessageList, "json");
    }

    function showMessageList(messageText) {
        let div = document.getElementById('chat-list');
        let innerHTML = '';
        for (let i = 0; i < messageText.length; i++) {
            innerHTML +=
                '<p>\n' +
                '   <div class="d-flex w-100 align-items-center justify-content-between">\n' +
                '       <strong class="mb-1">' + messageText[i].name + '</strong>\n' +
                '   </div>\n' +
                '   <div class="col-10 mb-1 small">' + messageText[i].date + '</div>\n' +
                '   <div class="col-10 mb-1 small">' + messageText[i].text + '</div>\n' +
                '</p>'
        }
        let innerDiv = document.createElement('div');
        innerDiv.innerHTML = innerHTML;
        div.insertAdjacentElement("beforeend", innerDiv);
        currentFirstMessageId = messageText[messageText.length - 1].id;
        block = false;
    }

});