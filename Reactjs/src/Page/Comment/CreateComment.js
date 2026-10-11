import React, { useState } from 'react';
import * as commentService from "../../Service/CommentService";
import { Button, Card, Container, Form } from 'react-bootstrap';
import ErrorMessage from "../../Component/SiteMessage/ErrorMessage";

export const CreateComment = (props) => {
  const [commentData, setCommentData] = useState({
    content: "",
    parentCommentId: props.replyTo,
    title: "",
    postId: parseInt(props.postId),
    isPrimeComment: true
  });

  const [error, setError] = useState(null)

  const handleInput = (e) => {
    e.preventDefault();
    const { name, value } = e.target;
    setCommentData((prevData) => ({ ...prevData, [name]: value }));
  };

  const sendData = async (e) => {
    e.preventDefault();
    const response = await commentService.saveComment(commentData);
    if (!response.error) {
      setError(response.error)
    }
  };

  if (error) {
    return <ErrorMessage message={error}/>
  }
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
                name='content'
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
