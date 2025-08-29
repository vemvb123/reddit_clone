import { useParams, Link } from 'react-router-dom'
import { Form, Button } from 'react-bootstrap';
import React, { useState } from 'react';
import * as postService from "../Services/PostService"


export default function CreatePost() {

  let params = useParams()
  const communityName = params.community_name;


  const [postData, setPostData] = useState({
    title: '',
    content: '',
  });










  const handleSubmit = (e) => {
    e.preventDefault();


    let token = localStorage.getItem("token");
    let response = postService.savePost(token, postData, communityName);



  };






  return (
    
    <div className="container mt-5">
      <h1>Create a Post</h1>
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="title">
          <Form.Label>Title</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter the title"
            value={postData.title}
            onChange={(e) => setPostData(prevData => ({ ...prevData, title: e.target.value }))}
          />
        </Form.Group>

        <Form.Group controlId="description">
          <Form.Label>Description</Form.Label>
          <Form.Control
            as="textarea"
            rows={20}
            placeholder="Enter the description"
            value={postData.content}
            onChange={(e) => setPostData(prevData => ({ ...prevData, content: e.target.value }))}
          />
        </Form.Group>

        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
    </div>
  )
}
