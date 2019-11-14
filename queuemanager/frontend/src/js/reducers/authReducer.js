import { UPDATE_VALUE_AUTH } from '../actions/actionTypes';
const initialState = {
  auth: {
    isAuthenticated: false,
    token: null,
    makerspaceId: null
  }
};
export const authReducer = (state = initialState, action) => {
  switch (action.type) {
    case UPDATE_VALUE_AUTH:
      return {
        ...state,
        auth: action.auth,
      };
    default:
      return state;
  }
};
