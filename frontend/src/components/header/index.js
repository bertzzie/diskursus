import { h, Component } from 'preact';
import { Link } from 'preact-router/match';
import style from './style';

export default class Header extends Component {
    static defaultProps = {
        userInfo: {
            isLoggedIn: false,
            name: null,
            email: null,
            picture: null,
            role: null
        }
    };

    loggedInMenu = () => {
        return (<Link activeClassName={style.active} href="/chat">Admin Chat</Link>)
    };

    loggedOutMenu = () => {
        return [
            (<Link activeClassName={style.active} href="/login">Login</Link>),
            (<Link activeClassName={style.active} href="/register">Register</Link>)
        ]
    };

	render() {
		return (
			<header class={style.header}>
				<h1>Diskursus</h1>
				<nav>
					<Link activeClassName={style.active} href="/">Home</Link>
                    { this.props.userInfo.isLoggedIn ? this.loggedInMenu() : this.loggedOutMenu() }
				</nav>
			</header>
		);
	}
}
