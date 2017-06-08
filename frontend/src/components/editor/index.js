import { h, Component } from 'preact';
import style from './style';

import Config from '../../Config';

import 'whatwg-fetch'

export default class NewPostEditor extends Component {
    state = {
        value: "",
        fileCount: 0
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

    handleFileChanged = evt => {
        this.setState({
            fileCount: evt.target.files.length
        });
    };

    render() {
        const user = this.props.userInfo;
        const label = this.state.fileCount > 0 ? `${this.state.fileCount} file dipilih` : "";

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

                    <input type="file"
                           name="image"
                           id="image"
                           class={style.imageField}
                           accept="image/*"
                           multiple={true}
                           onChange={this.handleFileChanged} />
                    <label for="image">
                        { label } <img src="/assets/icons/upload32x32.png" alt="Logo Upload"/>
                    </label>
                </div>

                <hr style="clear: both;"/>
            </form>
        );
    }
}
