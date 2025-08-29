import React, { useEffect, useState } from 'react'
import * as commentService from "../Services/CommentService"
import { useParams } from 'react-router-dom'
import { Button, Card, Collapse, Container, Modal, Form } from 'react-bootstrap';


export default function EditComment() {
    let params = useParams()
    const commentId = params.commentId

    const [Comment, setComment] = useState("")


    let token = localStorage.getItem('token');

    async function getComment() {
        let result = await commentService.getComment(token, commentId)
        setComment(result)
      }

      useEffect(() => {
        getComment()
        console.log("Her")
        console.log(Comment)
      }, [])
      

      const handleSubmit = (e) => {
        e.preventDefault();

        let response = commentService.saveComment(token, Comment)
        console.log(response)
        
    
      };

      const [showDeleteModal, setShowDeleteModal] = useState(false);
  
      const handleDeleteComment = () => {
        
            commentService.deleteComment(token, commentId)
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

    <Form onSubmit={handleSubmit}>

    <Form.Group controlId="title">
      <Form.Label>Title</Form.Label>
      <Form.Control
        type="text"
        placeholder="Enter the title"
        value={Comment.title}
        onChange={(e) => setComment(prevData => ({ ...prevData, title: e.target.value }))}
      />
    </Form.Group>






      {/* Post Image */}
      {Comment.pathToImage && (
        <div 
          className="position-relative"
          onClick={() => window.location.href = `/${Comment.postId}/set_comment_image/${Comment.id}`}
        >
          <Card.Img
            className="w-100"
            src={`http://localhost:8080/image/get_file_by_filename/${Comment.pathToImage}`}
          />
          <div className="image-overlay">
            <span>Change image</span>
          </div>
        </div>
      )}
      <Button href={`/${Comment.postId}/set_comment_image/${Comment.id}`}>Set image</Button>



      {/* Content */}
      <Form.Group controlId="description">
      <Form.Label>Description</Form.Label>
      <Form.Control
        as="textarea"
        rows={3}
        placeholder="Enter the description"
        value={Comment.description}
        onChange={(e) => setComment(prevData => ({ ...prevData, description: e.target.value }))}
      />
    </Form.Group>


    <Button variant="primary" type="submit">
      Submit
    </Button>



    </Form>

    <Button variant="link" className="text-muted" onClick={() => setShowDeleteModal(true)}>
        Delete comment
    </Button>

          {/* Delete Post Modal */}
          <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Delete Post</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Are you sure you want to delete this comment?
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDeleteComment}>
            Delete comment forever
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  )
}
