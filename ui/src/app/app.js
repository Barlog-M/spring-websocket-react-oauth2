import React from "react";
import {connect} from "react-redux";

import Spinner from "./containers/spinner";
import Error from "./containers/error";
import MainMenu from "./menu/main";

import Connection from "./ws/connection";

const App = props => (
	<div>
		<Connection/>
		<Spinner/>
		<Error/>
		<MainMenu/>
		{props.children}
	</div>
);

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => ({});

export default connect(mapStateToProps, mapDispatchToProps)(App);
