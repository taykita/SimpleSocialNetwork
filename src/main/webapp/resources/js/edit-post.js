let url = new URL(window.location.href);
let searchParams = new URLSearchParams(url.search.substring(1));
let postId = searchParams.get("id");

function Root() {
    return (
        <div id="post" className="row col-md-12">
            <PostText/>
            <Submit/>
        </div>
    )
}

class PostText extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            post: []
        };
    }

    componentDidMount() {
        fetch("posts/" + postId)
            .then(res => res.json())
            .then(
                (result) => {
                    this.setState({
                        isLoaded: true,
                        post: result
                    });
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            )
    }

    render() {
        const {error, isLoaded, post} = this.state;
        console.log(post);
        if (error) {
            return <div>Ошибка: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Загрузка...</div>;
        } else {
            return (
                <div className="form-floating">
                    <div className="input-group post-window">
                        <textarea id="text" className="form-control" name="text">{post.text}</textarea>
                    </div>
                </div>
            );
        }
    }
}

function Submit() {
    return (
        <button onClick={putPost} className="w-100 btn btn-lg btn-dark" type="submit">Опубликовать</button>
    )
}

function putPost() {
    const data = {
        text: $("#text").val(),
        id: postId
    };

    $.ajax({
        url:"posts",
        type:"PUT",
        data: JSON.stringify(data),
        contentType:"application/json",
        async: false,
        dataType:"json",
        success: function (data) {
            window.location.href = "main";
        },
        error: function() {
            window.location.href = "main";
        }

    })

}

ReactDOM.render(
    <Root/>,
    document.getElementById('root')
);