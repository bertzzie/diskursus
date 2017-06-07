import { h, Component } from 'preact';
import style from './style';

import Config from '../../Config';
import camera from '../../assets/icons/upload-32x32.png';

import 'whatwg-fetch'

export default class NewPostEditor extends Component {
    state = {
        value: ""
    };

    handleChange = evt => {
        this.setState({
            value: evt.target.value
        });
    };

    handleFormSubmit = evt => {
        evt.preventDefault();

        fetch(`${Config.Hostname}/post/add`, {
            method: 'POST',
            credentials: 'include',
            body: new FormData(document.querySelector("#new-comment-form"))
        }).then(resp => {
            if (resp.status === 201) {
                this.setState({
                    value: ""
                })
            }

            return resp
        }).then(_ => window.location.reload())
    };

    render() {
        const user = this.props.userInfo;
        return (
            <form class={style.editor}
                  id="new-comment-form"
                  encType="multipart/form-data"
                  onSubmit={this.handleFormSubmit}>
                <aside class={style.postAuthor}>
                    <img src={user.picture} alt={user.name} />
                    { user.name }
                </aside>

                <div class={style.formInputs}>
                    <textarea onChange={this.handleChange}
                              name="content"
                              value={this.state.value} />

                    <input type="submit" value="Post" />

                    <input type="file" name="image" id="image" class={style.imageField} />
                    <label for="image">
                        <img src={camera} alt="Logo Upload"/>
                    </label>
                </div>

                <hr style="clear: both;"/>
            </form>
        );
    }
}
