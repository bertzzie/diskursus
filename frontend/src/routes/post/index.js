import { h, Component } from 'preact';
import style from '../home/style';
import Config from '../../Config';

import 'whatwg-fetch'

import Comments from '../../components/comments';

export default class Post extends Component {
    static defaultProps = {
        userInfo: {
            isLoggedIn: false,
            name: null,
            email: null,
            picture: null,
            role: null
        },
        postId: ""
    };

    state = {
        post: {
            poster: {}
        }
    };

    componentDidMount() {
        const that = this;
        fetch(`${Config.Hostname}/post/view/${this.props.postId}`, { credentials: "include" })
            .then(r => r.json())
            .then(response => {
                that.setState({
                    post: response
                })
            })
    }

    render() {
        const post = this.state.post || {};
        if (!post._id) {
            return null;
        }

        return(
            <article class={style.post} style="padding: 56px 20px;">
                <aside class={style.postAuthor}>
                    <img src={post.poster.picture} alt={post.poster.name} />
                    { post.poster.name }
                </aside>
                <div class={style.postContent}>{ post.content }</div>

                <hr class={style.commentSeparator} />

                <Comments postId={post._id} userInfo={this.props.userInfo} commentCount={9999} />

                <hr class={style.postSeparator} />
            </article>
        )
    }
}
