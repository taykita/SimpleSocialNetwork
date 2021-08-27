$(document).off('.data-api')
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

function sendName() {
    const message = {
        text: $("#text").val(),
        chatId: chatId,
        name: $("#name").val(),
    };
    stompClient.send("/app/chat", {}, JSON.stringify(message));
}

$(function () {
    chatId = $("#chatId").val();
    connect();
    $( "#send" ).click(function() { sendName(); });
});