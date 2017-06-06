import { h, Component } from 'preact';
import style from './style';

import Config from '../../Config';
import NewCommentEditor from './editor';

import 'whatwg-fetch'

export default class Comments extends Component {
    static defaultProps = {
        postId: ""
    };

    state = {
        comments: []
    };

    componentDidMount() {
        const postId = this.props.postId;
        fetch(`${Config.Hostname}/post/${postId}/comments`)
            .then(r => r.json())
            .then(response => {
                this.setState({
                    comments: response
                })
            })
    }

    renderEmpty = () => {
        return (<div class={style.comment}>Belum ada komentar...</div>)
    };

    renderFull = () => {
        return this.state.comments.map(comment => (
            <div class={style.comment}>
                <aside class={style.commentAuthor}>
                    <img src={comment.poster.picture} alt={comment.poster.name} />
                    { comment.poster.name }
                </aside>
                <p>{ comment.content }</p>
                <hr class={style.commentClear} />
            </div>
        ))
    };

    render() {
        const commentList = this.state.comments.length === 0 ? this.renderEmpty() : this.renderFull()

        return (
            <div class={style.commentContainer}>
                { commentList }
                { this.props.userInfo.isLoggedIn &&
                      <NewCommentEditor postId={this.props.postId} userInfo={this.props.userInfo} />
                }
            </div>
        )
    }
}
