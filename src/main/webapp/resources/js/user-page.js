let currentCount = 1;
let url = new URL(window.location.href);
let searchParams = new URLSearchParams(url.search.substring(1));
let currentId = searchParams.get("id");
$(document).off('.data-api')
$(function () {

    $('#load-post').click(function () {
        let data = {count: currentCount, id: currentId};
        $.get("user-page/get-posts", data, success, "json");
    });

    function success(posts) {
        let div = document.getElementById('post');
        let innerHTML = '';
        for (let i = 0; i < posts.length; i++) {
            innerHTML +=
                '<div class="row">\n' +
                '   <div class="col-md-7">\n' +
                '       <div class="p-2">\n' +
                '           <div class="post">\n' +
                '               <h6 class="list">' + posts[i].userName + '</h6>\n' +
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
        currentCount += 10;
    }
});
