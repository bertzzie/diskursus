import { h, Component } from 'preact';
import style from './style';

export default class Gallery extends Component {
    static defaultProps = {
        images: []
    };

    render() {
        return (
            <div class={style.gallery}>
                { this.props.images.map(i => <img src={i} />) }
            </div>
        )
    }
}
