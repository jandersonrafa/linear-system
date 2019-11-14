import { createStore, compose } from 'redux';
import { Reducers } from '../reducers';
import { composeWithDevTools } from 'redux-devtools-extension';
// export const Store = createStore(Reducers);
import { throttle } from 'lodash';
import { loadState, saveState } from '../services/localStorageService'

// load data local storage
const persistedState = loadState()
// first reducers second load local storage data, three plugin dev tools
const store = createStore(Reducers, persistedState, compose(
    composeWithDevTools()
));
// const store = createStore(Reducers, composeWithDevTools());

// save state authState in local storage
store.subscribe(throttle(() => {
    saveState({
        authState: store.getState().authState
    });
}, 1000));

export default store;

// const store = createStore (Reducers, composeWithDevTools ( 
//   applyMiddleware (... middleware), 
//   // outros realçadores de loja, se houver 
// ));