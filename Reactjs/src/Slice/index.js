// store/index.js
import { createStore, combineReducers } from 'redux';
import userReducer from '../Reducer/userReducer'

const rootReducer = combineReducers({
  user: userReducer,
  // Add more reducers as needed
});

const store = createStore(rootReducer);

export default store;
