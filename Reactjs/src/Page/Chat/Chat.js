import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';

const Chat = () => {
  let params = useParams();
  let chatId = params.chatId;
  let user = params.user;

  const [message, setMessage] = useState('');
  const [stompClient, setStompClient] = useState(null);
  const messages = useSelector((state) => state.chat.messages);
  const dispatch = useDispatch();

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/websocket');
    const client = Stomp.over(socket);
    setStompClient(client);

    client.connect({}, () => {
      client.subscribe(`/topic/chat/${chatId}`, (message) => {
        const newMessage = JSON.parse(message.body);
        dispatch({ type: 'RECEIVE_MESSAGE', payload: newMessage });
      });
    });

    fetchChatMessages();

    return () => {
      client.disconnect();
    };
  }, [chatId]);

  let token = localStorage.getItem("token")

  const handleSendMessage = () => {
    if (stompClient && stompClient.connected) {
      stompClient.send(`/app/sendMessage/${chatId}`, {}, JSON.stringify({ content: message, sender: user }));
      setMessage('');
    } else {
      console.error('WebSocket connection not established yet.');
    }
  };

  const fetchChatMessages = async () => {
    const response = await fetch(`http://localhost:8080/api/chat/${chatId}/messages`);
    const data = await response.json();
    dispatch({ type: 'FETCH_MESSAGES', payload: data });
  };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100vh', maxWidth: '600px', margin: 'auto' }}>
      <div style={{ flex: 1, overflowY: 'auto', padding: '10px' }}>
        {messages.map((msg) => (
          <div
            key={msg.id}
            style={{
              display: 'flex',
              flexDirection: msg.sender === user ? 'row-reverse' : 'row',
              alignItems: 'flex-start',
              marginBottom: '8px',
            }}
          >
            <div
              style={{
                backgroundColor: msg.sender === user ? '#4caf50' : '#2196f3',
                color: 'white',
                padding: '8px',
                borderRadius: '8px',
                maxWidth: '70%',
              }}
            >
              <span style={{ fontWeight: 'bold' }}>{msg.sender}:</span> {msg.content}
            </div>
          </div>
        ))}
      </div>
      <div
  style={{
    position: 'fixed',
    bottom: 0,
    width: '100%',
    maxWidth: '500px', // Set a maximum width for the input field container
    margin: '0 auto', // Center the input field container
    display: 'flex',
    alignItems: 'center',
    padding: '10px',
    borderTop: '1px solid #ccc',
    backgroundColor: '#fff',
    zIndex: 1000, // Set a higher z-index to make sure it's on top
  }}
>
  <input
    type="text"
    value={message}
    onChange={(e) => setMessage(e.target.value)}
    style={{
      flex: 1,
      padding: '10px',
      borderRadius: '4px',
      border: '1px solid #ccc',
      marginRight: '10px',
      width: '100%', // Use 100% width to fill the container
    }}
    placeholder="Type your message..."
  />
  <button
    style={{
      padding: '10px',
      cursor: 'pointer',
      background: '#007bff',
      color: '#fff',
      border: 'none',
      borderRadius: '4px',
    }}
    onClick={handleSendMessage}
  >
    Send
  </button>
</div>

    </div>
  );
};

export default Chat;