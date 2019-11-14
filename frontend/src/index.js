//import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react'
import ReactDOM from 'react-dom'
import Home from './js/Home'
import './css/style.scss'
import 'materialize-css/dist/js/materialize.js'
import 'materialize-css/dist/css/materialize.css'
import { Provider } from 'react-redux';
import store from './js/store';

// Log message to console
ReactDOM.render(
  <Provider store={store}>
  <Home />
</Provider>,
  document.getElementById('root')
);
// Needed for Hot Module Replacement
if(module.hot) {
  module.hot.accept() 
}