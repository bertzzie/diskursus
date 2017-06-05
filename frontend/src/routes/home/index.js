import { h, Component } from 'preact';
import style from './style';

import 'whatwg-fetch'

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
    };

	componentDidMount() {
	    const that = this;
		fetch("http://localhost:8082/post/list", { credentials: "include" })
            .then(r => r.json())
            .then(response => {
                that.setState({
                    posts: response
                })
            })
	}

	renderPost = post => {
	    return(
	        <article class={style.post}>
                <aside class={style.postAuthor}>
                    <img src={post.poster.picture} alt={post.poster.name} />
                    { post.poster.name }
                </aside>
                <p>{ post.content }</p>

                <hr style="clear: both;" />
            </article>
        )
    };

	render() {
		return (
			<div class={style.home}>
                {
                    (this.props.userInfo.isLoggedIn) ? <NewPostEditor userInfo={this.props.userInfo} /> : null
                }
                { this.state.posts.map(this.renderPost) }
			</div>
		);
	}
}
