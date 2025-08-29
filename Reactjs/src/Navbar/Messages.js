import React, { useEffect, useState } from 'react'
import Card from 'react-bootstrap/Card';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import Button from 'react-bootstrap/Button';
import NavDropdown from 'react-bootstrap/NavDropdown';
import * as messageService from "../Services/MessageService"
import * as userSerivce from "../Services/UserService"


export default function Messages(User) {



    let token = localStorage.getItem('token');

    const [Messages, setMessages] = useState([])
    

    async function getIntervallOfMessages() {

    
      let result = await messageService.get10LatestMessages(token, 0)
      setMessages((prevMessages) => [...prevMessages, ...result]);
      console.log("hallo")
      console.log(Messages)

    }

    

    useEffect(() => {
        getIntervallOfMessages();
        console.log(Messages)
      }, []);






    


      const printMessages = () => {
        console.log(Messages)
      }

      const handleAcceptRequest = (fromUsername) => {
        userSerivce.acceptFriendRequest(token, fromUsername)
      };

      const deleteMessage = (messageId) => {
        messageService.deleteMessage(token, messageId)
      };


  return (
    <NavDropdown title="Messages" id="collapsible-nav-dropdown">

    <Card>
    {Messages.map((message) => {
            return <NavDropdown.Item>
                {message.content}
                {message.messageTopic === 'NewFriendRequest' && (
                  <Button onClick={() => handleAcceptRequest(message.fromUsername)}>
                      Accept Request
                  </Button>
            )}
            <Button onClick={() => deleteMessage(message.messageId)}>Delete</Button>          
            </NavDropdown.Item>
        })}

  </Card>


  </NavDropdown>
  )
}
