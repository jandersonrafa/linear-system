import React, { Component } from 'react';
import { HashLink as Link } from 'react-router-hash-link';
import { HashRouter as Router, Route, Redirect } from "react-router-dom";
import { connect } from 'react-redux';
import { updateAuth } from './actions';
import { bindActionCreators } from 'redux';
import { Navbar, Row, Col } from 'react-materialize';

import Gauss from './Gauss'

class Home extends Component {
    constructor(props) {
        super(props);

        this.myRef = React.createRef();
        // this.logout = this.logout.bind(this);
    }

    closeMenu() {
        const menuMobile = document.getElementById('mobile-nav')
        if (menuMobile) {
            document.getElementsByClassName("sidenav-overlay")[0].click()
        }
    }

    render() {
        return (
            <div >
                <Router>
                    <header id="section-header">
                        <div id="section-header-navbar">

                            <Navbar alignLinks="right">
                            </Navbar>
                        </div>
                    </header>


                    <section id="section-content" >
                        <div class="row">
                            <Row>
                                <Col m={12} s={12}>
                                    <Route path="/" exact component={Gauss} />
                                </Col>
                            </Row>
                        </div>
                    </section>

                    <div id="section-footer">
                        <div class="footer">
                            <div class="content"> Desenvolvido por <a class="author" href="https://github.com/jandersonrafa">Janderson Rafael</a></div>
                        </div>
                    </div>
                </Router>
            </div>
        );
    }
}

const mapStateToProps = store => ({
    auth: store.authState.auth
});
const mapDispatchToProps = dispatch =>
    bindActionCreators({ updateAuth }, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(Home);