import { h, Component } from 'preact';
import { Router } from 'preact-router';
import Config from '../Config';

import 'whatwg-fetch';

import Header from './header';
import Chat from '../routes/chat';
import Home from '../routes/home';
import Login from '../routes/login';
import Post from '../routes/post';
import Register from '../routes/register';
// import Home from 'async!./home';
// import Profile from 'async!./profile';

export default class App extends Component {
    state = {
        userInfo: {
            isLoggedIn: false,
            name: null,
            email: null,
            picture: null,
            role: null
        },
    };

    componentDidMount() {
        this.updateUserInfo();
    }

    updateUserInfo = () => {
        const that = this;
        fetch(`${Config.Hostname}/user/info`, { credentials: "include" })
            .then(r => r.json())
            .then(response => {
                that.setState({
                    userInfo: response
                });
            })
            .catch(err => console.log(err));
    };

	/** Gets fired when the route changes.
	 *	@param {Object} event		"change" event from [preact-router](http://git.io/preact-router)
	 *	@param {string} event.url	The newly routed URL
	 */
	handleRoute = e => {
		this.currentUrl = e.url;
		this.updateUserInfo();
	};

	render() {
		return (
			<div id="app">
				<Header userInfo={this.state.userInfo} />
				<Router onChange={this.handleRoute}>
					<Home path="/" userInfo={this.state.userInfo} />
					<Chat path="/chat/" user="me" />
                    <Login path="/login/" />
                    <Post path="/post/:postId" />
                    <Register path="/register" />
				</Router>
			</div>
		);
	}
}
