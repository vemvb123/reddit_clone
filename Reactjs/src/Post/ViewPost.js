import React, { useEffect, useState } from 'react';
import { Comments } from '../Comment/Comments';
import CreateCommunity from '../Community/CreateCommunity';
import { CreateComment } from '../Comment/CreateComment';
import { useParams } from 'react-router-dom';
import { Button, Card, Collapse, Container, Modal } from 'react-bootstrap';
import * as commentService from '../Services/CommentService';
import * as postService from '../Services/PostService';
import RoundImage from '../Community/RoundImage';

export default function ViewPost() {
  let token = localStorage.getItem('token');

  const [comments, setComments] = useState([]);

  useEffect(() => {
    getPost();
    getIntervallOfComments();
  }, []);

  let params = useParams();
  const postId = params.postId;

  const [post, setPost] = useState('');

  async function getPost() {
    let response = await postService.getPost(token, postId);
    setPost(response);
    console.log("post:")
    console.log(response);
  }

  const [showMoreCommentsButton, setShowMoreCommentsButtonSomeState] = useState(true);

  function toggleShowMoreCommentsButton() {
    setShowMoreCommentsButtonSomeState(!showMoreCommentsButton);
  }

  const [highestId, setHighestId] = useState(0);

  async function getIntervallOfComments() {
    let postId = params.postId;
    let fromPostId = highestId;
    let parentCommentId = 0;

    let result = await commentService.getIntervallOfComments(token, postId, fromPostId, parentCommentId);
    result = result.filter((comment) => !comments.includes(comment));
    setComments([...comments, ...result]);

    for (let i = 0; i < result.length; i++) {
      setHighestId(result[i].id);
    }
  }

  let defaultImageUrl = 'https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg';

















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
                text={"See community"}

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
                text={"See profile"}

                to={`/profile/${post.username}`}
                sizeInPixels={'50px'}
              />
            </div>
            <p className="mb-0">{post.username}</p>
          </div>

          {/* Title */}
          <Card.Title>{post.title}</Card.Title>

          {/* Post Image */}
          {post.pathToPostImage && (

              <Card.Img
                className="w-100"
                src={`http://localhost:8080/image/get_file_by_filename/${post.pathToPostImage}`}
              />

          )}

          {/* Content */}
          <Card.Text style={{ whiteSpace: 'pre-line' }}>{post.content}</Card.Text>
          
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

            <Button variant="link" className="text-muted" href={`/${post.communityName}/edit_post/${postId}`}>
                    Edit post
            </Button>
            </span>
            </Card.Text>
        </Card.Body>
      </Card>







    
      <CreateComment postId={params.postId} className="mt-5"></CreateComment>
      <Comments comments={comments} toggleShowMoreCommentsButton={toggleShowMoreCommentsButton} />

      <Collapse in={showMoreCommentsButton}>
        <div>
          <Button onClick={getIntervallOfComments}>Get 10 more comments</Button>
        </div>
      </Collapse>
    </Container>
  );
}
