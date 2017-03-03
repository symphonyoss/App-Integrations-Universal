import React from 'react';
import ReactDOM from 'react-dom';
import './styles/styles.css';
import './js/scripts.js';
export default class Setup extends React.Component {
	render() {
		return(
			<div>
				{/* Start editing area */}
				<p>Refer to the <a href='https://integrations.symphony.com/docs/universal-webhook-integrations' target='blank'>Universal Webhook</a> to learn more how to build your own webhook.</p>
				{/* End editing area */}
			</div>
		);
	}
}