import React, { Component } from 'react';


class HighlightedButton extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div class="highlighted-button">
                <a class="highlighted-button__a" onClick={this.props.onClick} href={this.props.href} target="_blank" >{this.props.text}</a>
            </div>
        );
    }
}
export default HighlightedButton;