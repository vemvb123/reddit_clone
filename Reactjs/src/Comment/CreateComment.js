import React, { useState } from 'react';
import * as commentService from "../Services/CommentService";
import { Button, Card, Container, Form } from 'react-bootstrap';

export const CreateComment = (props) => {
  const [commentData, setCommentData] = useState({
    description: "",
    parentCommentId: props.replyTo,
    title: "",
    postId: parseInt(props.postId),
    isPrimeComment: true
  });

  const handleInput = (e) => {
    e.preventDefault();
    const { name, value } = e.target;
    setCommentData((prevData) => ({ ...prevData, [name]: value }));
  };

  let token = localStorage.getItem("token");


  

  const sendData = (e) => {
    e.preventDefault();
    commentService.saveComment(token, commentData);
  };

  return (
    <Card className="my-3">
      <Card.Body>
        <Container>
          <Form>
            <Form.Group controlId="title" className="mb-3">
              <Form.Control
                placeholder='Write title'
                type="text"
                name='title'
                onChange={handleInput}
                required
              />
            </Form.Group>

            <Form.Group controlId="description" className="mb-3">
              <Form.Control
                placeholder='Write a comment'
                as="textarea"
                rows={10}
                cols={25}
                name='description'
                onChange={handleInput}
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Button onClick={sendData}>Create comment</Button>
            </Form.Group>
          </Form>
        </Container>
      </Card.Body>
    </Card>
  );
};
