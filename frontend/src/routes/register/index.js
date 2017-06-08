import { h, Component } from 'preact';
import style from './style';
import formStyle from '../login/style';
import Config from '../../Config';

export default class Register extends Component {
    state = {
        username: "",
        password: "",
        retypePassword: "",
        error: ""
    };

    handleUsernameChange = evt => {
        this.setState({
            username: evt.target.value
        })
    };

    handlePasswordChange = evt => {
        this.setState({
            password: evt.target.value
        })
    };

    handleRetypePasswordChange = evt => {
        this.setState({
            retypePassword: evt.target.value
        })
    };

    handleFormSubmit = e => {
        e.preventDefault();

        fetch(`${Config.Hostname}/user/register`, {
            method: "POST",
            credentials: "include",
            body: new FormData(document.querySelector("#register-form"))
        })
        .then(r => r.json())
        .then(r => {
            if(r.cause) {
                this.setState({
                    error: r.cause
                });
            } else {
                window.location = "/"
            }
        })
    };

    render() {
        return (
            <form id="register-form" class={style.register} onSubmit={this.handleFormSubmit}>
                <h3>Register Form</h3>

                {
                    this.state.error === "" ? null :
                        <div style="color: red;">Error: { this.state.error }</div>
                }

                <div class={formStyle.formInput}>
                    <label htmlFor="username">Username:</label>
                    <input type="text"
                           id="username"
                           name="username"
                           value={this.state.username}
                           onChange={this.handleUsernameChange} />
                </div>

                <div class={formStyle.formInput}>
                    <label htmlFor="password">Password:</label>
                    <input type="password"
                           id="password"
                           name="password"
                           value={this.state.password}
                           onChange={this.handlePasswordChange} />
                </div>

                <div class={formStyle.formInput}>
                    <label htmlFor="retypePassword">Ulangi:</label>
                    <input type="password"
                           id="retypePassword"
                           name="retypePassword"
                           value={this.state.password}
                           onChange={this.handleRetypePasswordChange} />
                </div>

                <div class={formStyle.formActions}>
                    <input type="reset" value="Bersihkan" />
                    <input type="submit" value="Register" />
                </div>
            </form>
        )
    }
}
