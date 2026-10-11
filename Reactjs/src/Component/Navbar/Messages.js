import React, { useEffect, useState } from 'react'
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import NavDropdown from 'react-bootstrap/NavDropdown';
import * as messageService from "../../Service/MessageService"
import * as userSerivce from "../../Service/UserService"


export default function Messages() {

    const [Messages, setMessages] = useState([])

    async function getIntervallOfMessages() {
        const messages = await messageService.get10LatestMessages(0)
        if (!messages.error) {
            setMessages((prevMessages) => [...prevMessages, ...messages.data]);
        }
    }

    useEffect(() => {
        getIntervallOfMessages();
    }, []);


    const handleAcceptRequest = (fromUsername) => {
        userSerivce.acceptFriendRequest(fromUsername)
    };

    const deleteMessage = (messageId) => {
        messageService.deleteMessage(messageId)
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
