let currentCount = 1;
$(document).off('.data-api')
$(function () {
    $(document).ready(get);
    $('#load-post').click();

    function get() {
        let data = {count: currentCount};
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
                '               <h6 class="list">' + posts[i].userName + '</h6>\n' +
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
        currentCount += 10;
    }
});