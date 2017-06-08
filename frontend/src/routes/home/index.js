import { h, Component } from 'preact';
import style from './style';
import Config from '../../Config';

import 'whatwg-fetch'

import Comments from '../../components/comments';
import Gallery from '../../components/gallery';
import NewPostEditor from '../../components/editor';

export default class Home extends Component {
    static defaultProps = {
        userInfo: {
            isLoggedIn: false,
            name: null,
            email: null,
            picture: null,
            role: null
        }
    };

    state = {
        posts: [],
        hasMore: false,
    };

	componentDidMount() {
	    const that = this;
		fetch(`${Config.Hostname}/post/list`, { credentials: "include" })
            .then(r => r.json())
            .then(response => {
                that.setState({
                    posts: response
                })
            })
	}

	loadMore = () => {
        const that = this;
        const cursor = this.state.posts[this.state.posts.length - 1]['_id'];
        fetch(`${Config.Hostname}/post/list?cursor=${cursor}`, { credentials: "include" })
            .then(r => r.json())
            .then(response => {
                that.setState({
                    posts: this.state.posts.concat(response),
                    hasMore: response.length !== 0
                })
            })
    };

	renderPost = post => {
        return(
            <article class={style.post}>
                <aside class={style.postAuthor}>
                    <img src={post.poster.picture} alt={post.poster.name} />
                    { post.poster.name }
                </aside>
                <div class={style.postContent}>{ post.content }</div>

                <hr class={style.commentSeparator} />

                <Gallery images={post.pictures} />

                <Comments postId={post._id} userInfo={this.props.userInfo} />

                <hr class={style.postSeparator} />
            </article>
        )
    };

	render() {
		return (
			<div class={style.home}>
                {
                    this.props.userInfo.isLoggedIn ? <NewPostEditor userInfo={this.props.userInfo} /> : null
                }

                {
                    this.state.posts.length > 0 ?
                        this.state.posts.map(this.renderPost) :
                        <p>Belum ada post.</p>
                }

                {
                    this.state.hasMore && this.state.posts.length > 0 ?
                      <div class={style.loadMore}>
                          <input type="button"
                                 value="Lihat Selebihnya"
                                 onClick={this.loadMore} />
                      </div> :
                      null
                }
			</div>
		);
	}
}
