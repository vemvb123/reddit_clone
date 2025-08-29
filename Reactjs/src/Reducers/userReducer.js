// userReducer.js
import { PRINT_STATE, SET_USER, SET_USER_PROFILE_IMAGE, SET_USER_TOKEN, SET_USER_USERNAME } from '../Actions/userActionTypes';

const initialState = {
  username: "",
  pathToProfileImage: "",
  token: "",
};

const userReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_USER:
      // Set user-related fields in the state
      return {
        ...state,
        username: action.payload.username,
        pathToProfileImage: action.payload.pathToProfileImage,
        token: action.payload.token,
      };

    case SET_USER_TOKEN:
      // Set user token in the state
      return {
        ...state,
        token: action.payload.token,
      };

    case SET_USER_PROFILE_IMAGE:
      // Set user profile image in the state
      return {
        ...state,
        pathToProfileImage: action.payload.pathToProfileImage,
      };

    case SET_USER_USERNAME:
      // Set user username in the state
      return {
        ...state,
        username: action.payload.username,
      };


    case PRINT_STATE:
        // Set user username in the state
        console.log("oisan")
        console.log(state)
        
        return {
            ...state,
        };

    default:
      return state;
  }
};

export default userReducer;
