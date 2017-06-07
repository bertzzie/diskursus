import { h, Component } from 'preact';
import style from './style';
import Config from '../../Config';

import 'whatwg-fetch'

export default class Login extends Component {
    state = {
        username: "",
        password: ""
    };

    handleUsernameChange = e => {
        this.setState({ username: e.target.value });
    };

    handlePasswordChange = e => {
        this.setState({ password: e.target.value });
    };

    handleFormSubmit = e => {
        e.preventDefault();

        fetch(`${Config.Hostname}/user/authenticate`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: this.state.username,
                password: this.state.password
            })
        })
        .then(response => response.json())
        .then(r => {
            if(r.success) {
                window.location = "/";
            }
        })
    };

    render() {
        return (
            <form id="login-form" class={style.login} onSubmit={this.handleFormSubmit}>
                <h3>Masukkan data login</h3>

                <div class={style.formInput}>
                    <label htmlFor="username">Username:</label>
                    <input type="text"
                           id="username"
                           name="username"
                           value={this.state.username}
                           onChange={this.handleUsernameChange} />
                </div>

                <div class={style.formInput}>
                    <label htmlFor="password">Password:</label>
                    <input type="password"
                           id="password"
                           name="password"
                           value={this.state.password}
                           onChange={this.handlePasswordChange} />
                </div>

                <div class={style.formActions}>
                    <input type="reset" value="Bersihkan" />
                    <input type="submit" value="Login" />
                </div>
            </form>
        )
    }
}
