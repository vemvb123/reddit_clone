// chatReducer.js
const initialState = {
    messages: [],
  };
  
  const chatReducer = (state = initialState, action) => {
    switch (action.type) {
      case 'RECEIVE_MESSAGE':
        return {
          ...state,
          messages: [...state.messages, action.payload],
        };
      case 'SEND_MESSAGE':
        return {
          ...state,
          messages: [...state.messages, action.payload],
        };
      case 'FETCH_MESSAGES':
        return {
          ...state,
          messages: action.payload,
        };
      default:
        return state;
    }
  };
  
  export default chatReducer;
  