import React, { useState } from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { Link, useParams } from 'react-router-dom';
import * as communityService from "../Services/CommunityService"


export default function CommunitySettings() {

    let params = useParams()
    let communityName = params.community_name
    let token = localStorage.getItem('token');


  const [banFormData, setBanFormData] = useState({
    username: '',
    deletePosts: false,
    deleteComments: false,
  });

  const [descriptionFormData, setDescriptionFormData] = useState({
    description: '',
  });

  const handleBanSubmit = (e) => {
    e.preventDefault();
    // Add logic to handle ban user form submission
    communityService.banUser(token, communityName, banFormData.username) //videre å gjøre: så man også kan slette alle posts og comments
    
  };

  const handleDescriptionSubmit = (e) => {
    e.preventDefault();
    // Add logic to handle change community description form submission
    console.log('Change Community Description Form Submitted:', descriptionFormData);
  };

  return (
    <div>
      <h2>Community Settings</h2>

      {/* Ban User Form */}
      <Form onSubmit={handleBanSubmit}>
        <Form.Group controlId="banFormUsername">
          <Form.Label>Ban user</Form.Label>
          <Form.Control
            type="text"
            placeholder="Write the username of the user to ban"
            value={banFormData.username}
            onChange={(e) => setBanFormData({ ...banFormData, username: e.target.value })}
          />
        </Form.Group>

        <Form.Group controlId="banFormDeletePosts">
          <Form.Check
            type="checkbox"
            label="Delete all the user's posts"
            checked={banFormData.deletePosts}
            onChange={(e) => setBanFormData({ ...banFormData, deletePosts: e.target.checked })}
          />
        </Form.Group>

        <Form.Group controlId="banFormDeleteComments">
          <Form.Check
            type="checkbox"
            label="Delete all the user's comments"
            checked={banFormData.deleteComments}
            onChange={(e) => setBanFormData({ ...banFormData, deleteComments: e.target.checked })}
          />
        </Form.Group>

        <Button variant="primary" type="submit">
          Ban User
        </Button>
      </Form>

      <hr />

      {/* Make Announcement Post Button */}
      <Link to="/announcement">
        <Button variant="success">Make Announcement Post</Button>
      </Link>

      <hr />

      {/* Delete Community Button */}
      <Button variant="danger">Delete Community</Button>

      <hr />

      {/* Change Community Description Form */}
      <Form onSubmit={handleDescriptionSubmit}>
        <Form.Group controlId="descriptionForm">
          <Form.Label>Change community description</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            placeholder="Write description"
            value={descriptionFormData.description}
            onChange={(e) => setDescriptionFormData({ ...descriptionFormData, description: e.target.value })}
          />
        </Form.Group>

        <Button variant="primary" type="submit">
          Save Description
        </Button>
        <hr />

        <Button href='set_wallpaper'>
            Set community wallpaper
        </Button>
        <hr />

        <Button href='set_logo'>
            Set community logo
        </Button>


      </Form>
    </div>
  );
}
