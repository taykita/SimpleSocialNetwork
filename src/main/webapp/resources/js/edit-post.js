
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
                        <textarea id="text" className="form-control" name="text" value={post.text}/>
                    </div>
                </div>
            );
        }
    }
}

function Submit() {
    return (
        <form>
            <button onClick={putPost} className="w-100 btn btn-lg btn-dark" type="submit">Опубликовать</button>
        </form>
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
        dataType:"json",
        success: function (data) {
            window.location.href = "main";
        },
        error: function() {
            ReactDOM.render(
                <h1>Ошибка обновления поста</h1>,
                document.getElementById('post')
            );
        }

    })
}

ReactDOM.render(
    <Root/>,
    document.getElementById('root')
);