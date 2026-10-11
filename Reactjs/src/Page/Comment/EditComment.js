import React, { useEffect, useState } from 'react'
import * as commentService from "../../Service/CommentService"
import { useParams } from 'react-router-dom'
import { Button, Card, Container, Modal, Form } from 'react-bootstrap';
import ErrorMessage from "../../Component/SiteMessage/ErrorMessage";
import LoadingMessage from "../../Component/SiteMessage/LoadingMessage";


export default function EditComment() {
    let params = useParams()
    const commentId = params.commentId

    const [comment, setComment] = useState(null)
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [error, setError] = useState(null)
    const [loading, setLoading] = useState(true)

    const getComment = async (e) => {
        const result = await commentService.getComment(commentId);
        if (!result.error) {
            setComment(result.data)
        } else {
            setError(result.error.message)
        }
        setLoading(false)
    }

    useEffect(() => {
        getComment()
    }, [])
      

    const handleSubmit = (e) => {
        e.preventDefault();
        commentService.saveComment(comment)
      };


      const handleDeleteComment = () => {
            commentService.deleteComment(commentId)
      };

    if (error) {
        return <ErrorMessage message={error}/>
    }
    if (loading) {
        return <LoadingMessage/>
    }

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
        value={comment.title}
        onChange={(e) => setComment(prevData => ({ ...prevData, title: e.target.value }))}
      />
    </Form.Group>



      {/* Post Image */}
      {comment.pathToImage && (
        <div
          className="position-relative"
          onClick={() => window.location.href = `/${comment.postId}/set_comment_image/${comment.id}`}
        >
          <Card.Img
            className="w-100"
            src={`http://localhost:8080/image/${comment.pathToImage}`}
          />
          <div className="image-overlay">
            <span>Change image</span>
          </div>
        </div>
      )}
      <Button href={`/${comment.postId}/set_comment_image/${comment.id}`}>Set image</Button>



      {/* Content */}
      <Form.Group controlId="content">
      <Form.Label>Description</Form.Label>
      <Form.Control
        as="textarea"
        rows={3}
        placeholder="Enter the description"
        value={comment.content}
        onChange={(e) => setComment(prevData => ({ ...prevData, content: e.target.value }))}
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
