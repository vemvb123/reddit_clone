import React, { useEffect, useState } from 'react';
import { Comments } from '../Comment/Comments';
import CreateCommunity from '../Community/CreateCommunity';
import { CreateComment } from '../Comment/CreateComment';
import { useParams } from 'react-router-dom';
import { Button, Card, Collapse, Container, Modal, Form } from 'react-bootstrap';
import * as commentService from '../Services/CommentService';
import * as postService from '../Services/PostService';
import RoundImage from '../Community/RoundImage';

export default function EditPost() {



    


    let token = localStorage.getItem('token');

  
    useEffect(() => {
      getPost();
    }, []);
  
    let params = useParams();
    const postId = params.postId;
    const communityName = params.community_name;

  
    const [post, setPost] = useState('');
  
    async function getPost() {
      let response = await postService.getPost(token, postId);
      setPost(response);

    }
  


  

  
    let defaultImageUrl = 'https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg';
  
  
  
  

  
  
  
  
  
    const [showDeleteModal, setShowDeleteModal] = useState(false);
  
    const handleDeletePost = () => {
      // Perform the delete operation here
      postService.deletePost(token, postId)
  
      // Close the modal after deletion
      setShowDeleteModal(false);
    };


    const handleSubmit = (e) => {
        e.preventDefault();
    
    
        let token = localStorage.getItem("token");
        let response = postService.savePost(token, post, communityName);
    
    
    
      };


  return (
    <Container>
      <style>
        {`
          .my-3 {
            margin-bottom: 1rem !important;
          }

          .image-overlay {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: rgba(0, 0, 0, 0.3);
            opacity: 0;
            transition: opacity 0.3s ease-in-out;
          }

          .position-relative:hover .image-overlay {
            opacity: 1;
          }

          .image-overlay span {
            color: white;
            font-size: 18px;
            cursor: pointer;
          }
        `}
      </style>
      <Card className="my-3">
        <Card.Body>
          {/* Line 1: Round Image for Community */}
          <div className="d-flex align-items-center mb-2">
            <div className="me-3">
              <RoundImage
                imageUrl={
                  post.pathToCommunityImage
                    ? `http://localhost:8080/image/get_file_by_filename/${post.pathToCommunityImage}`
                    : defaultImageUrl
                }
                to={`/community/${post.communityName}`}
                sizeInPixels={'50px'}
              />
            </div>
            <p className="mb-0">{post.communityName}</p>
            <span className="ms-2">...posted by</span>
            {/* Round Image for User */}
            <div className="me-3">
              <RoundImage
                imageUrl={
                  post.pathToUserImage
                    ? `http://localhost:8080/image/get_file_by_filename/${post.pathToUserImage}`
                    : defaultImageUrl
                }
                to={`/profile/${post.username}`}
                sizeInPixels={'50px'}
              />
            </div>
            <p className="mb-0">{post.username}</p>
          </div>

          {/* Title */}
          <Form onSubmit={handleSubmit}>

        <Form.Group controlId="title">
          <Form.Label>Title</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter the title"
            value={post.title}
            onChange={(e) => setPost(prevData => ({ ...prevData, title: e.target.value }))}
          />
        </Form.Group>







          {/* Post Image */}
          {post.pathToPostImage && (
            <div 
              className="position-relative"
              onClick={() => window.location.href = `/${postId}/set_image`}
            >
              <Card.Img
                className="w-100"
                src={`http://localhost:8080/image/get_file_by_filename/${post.pathToPostImage}`}
              />
              <div className="image-overlay">
                <span>Change image</span>
              </div>
            </div>
          )}

          {/* Content */}
          <Form.Group controlId="description">
          <Form.Label>Description</Form.Label>
          <Form.Control
            as="textarea"
            rows={20}
            placeholder="Enter the description"
            value={post.content}
            onChange={(e) => setPost(prevData => ({ ...prevData, content: e.target.value }))}
          />
        </Form.Group>


        <Button variant="primary" type="submit">
          Submit
        </Button>


        </Form>
        
          {/* createdAt */}
          <Card.Text className="text-muted small">
            {post.createdAt}
            {post.pathToPostImage === null && (
                <span className="d-block mt-2">
                <Button href={`/${postId}/set_image`} variant="link" className="text-muted">
                    Set image for post
                </Button>
                </span>
            )}
            <span className="d-block mt-2">
            <Button variant="link" className="text-muted" onClick={() => setShowDeleteModal(true)}>
                    Delete post
            </Button>
            </span>
            </Card.Text>
        </Card.Body>
      </Card>



      {/* Delete Post Modal */}
      <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Delete Post</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Are you sure you want to delete this post?
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDeletePost}>
            Delete post forever
          </Button>
        </Modal.Footer>
      </Modal>






    </Container>
  )
}
