import { h, Component } from 'preact';
import style from './style';

import Config from '../../Config';

import 'whatwg-fetch'

export default class NewCommentEditor extends Component {
    state = {
        value: ""
    };

    handleChange = evt => {
        this.setState({
            value: evt.target.value
        });
    };

    handleEnterPressed = evt => {
        if(evt.key === 'Enter') {
            fetch(`${Config.Hostname}/comment/add`, {
                method: 'PUT',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    'postId': this.props.postId,
                    'content': evt.target.value,
                    'poster': {
                        name: this.props.userInfo.name,
                        email: this.props.userInfo.email,
                        picture: this.props.userInfo.picture,
                        role: this.props.userInfo.role
                    }
                })
            }).then(resp => {
                if (resp.status === 201) {
                    this.setState({
                        value: ""
                    })
                }

                return resp.json()
            }).then(_ => window.location.reload())
        }
    };

    render() {
        const user = this.props.userInfo;
        return (
            <div class={style.editor}>
                <aside class={style.postAuthor}>
                    <img src={user.picture} alt={user.name} />
                    { user.name }
                </aside>

                <textarea onChange={this.handleChange}
                          onKeyPress={this.handleEnterPressed}
                          value={this.state.value} />
            </div>
        );
    }
}
