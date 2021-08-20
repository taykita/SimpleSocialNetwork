let currentFirstPostId = 2147483647;
$(document).off('.data-api')
$(function () {
    $(document).ready(get);
    $('#load-post').click(get);

    function get() {
        let data = {firstPostId: currentFirstPostId};
        $.get("news/get-posts", data, success, "json");
    }

    // $(window).scroll(function() {
    //
    //     let target = $(this).scrollTop();
    //
    //     if(target == 1) {
    //         get();
    //     }
    //
    // });

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
        currentFirstPostId = posts[posts.length-1].id;
    }
});