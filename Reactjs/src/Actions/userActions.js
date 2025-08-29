// actions.js
import { SET_USER_PROFILE_IMAGE, SET_USER_USERNAME, SET_USER_TOKEN, SET_USER, PRINT_STATE } from './userActionTypes'

export const setUserToken = (token) => ({
  type: SET_USER_TOKEN,
  payload: { token },
});

export const setUserUsername = (username) => ({
    type: SET_USER_USERNAME,
    payload: { username },
  });

export const setUserProfileImage = (profileImage) => ({
type: SET_USER_PROFILE_IMAGE,
payload: { profileImage },
});

export const setUser = (username, profileImage, token) => ({
type: SET_USER,
payload: { username, profileImage, token },
});



export const printState = () => ({
    type: PRINT_STATE,
    payload: {  },
    });
