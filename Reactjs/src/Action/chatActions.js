// chatActions.js
export const receiveMessage = (message) => ({
    type: 'RECEIVE_MESSAGE',
    payload: message,
  });
  
  export const sendMessage = (message) => ({
    type: 'SEND_MESSAGE',
    payload: message,
  });
  
  export const fetchMessages = (messages) => ({
    type: 'FETCH_MESSAGES',
    payload: messages,
  });
  