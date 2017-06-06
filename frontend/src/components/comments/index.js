import { h, Component } from 'preact';
import style from './style';

import Config from '../../Config';

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
        return (
            <div class={style.commentContainer}>
                { this.state.comments.length === 0 ? this.renderEmpty() : this.renderFull() }
            </div>
        )
    }
}
