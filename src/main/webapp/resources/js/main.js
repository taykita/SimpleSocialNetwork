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
$(document).off('.data-api')
$(function () {
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

    $('.upload_files').on( 'click', function( event ){

    	event.stopPropagation();
    	event.preventDefault();

    	if( typeof files == 'undefined' ) return;

    	var data = new FormData();

    	$.each( files, function( key, value ){
    		data.append( key, value );
    	});

    	data.append( 'my_file_upload', 1 );

    	$.ajax({
    		url         : 'upload',
    		type        : 'POST',
    		data        : data,
    		cache       : false,
    		dataType    : 'json',
    		processData : false,
    		contentType : false,
    		success     : function( respond, status, jqXHR ){

    			if( typeof respond.error === 'undefined' ){
    				var files_path = respond.files;
    				var html = '';
    				$.each( files_path, function( key, val ){
    					 html += val +'<br>';
    				} )

    				$('.ajax-reply').html( html );
    			}
    			else {
    				console.log('ОШИБКА: ' + respond.data );
    			}
    		},
    		error: function( jqXHR, status, errorThrown ){
    			console.log( 'ОШИБКА AJAX запроса: ' + status, jqXHR );
    		}

    	});

    });

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