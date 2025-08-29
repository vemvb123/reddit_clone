import React, { useEffect, useState } from 'react';
import * as postService from '../Services/PostService';
import { Card, Container, Row, Button } from 'react-bootstrap';
import RoundImage from './RoundImage';
import { Link } from 'react-router-dom';

export default function ViewCommunityPostsMemberOf() {
  const [posts, setPosts] = useState([]);
  const [page, setPage] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await postService.getPostsMemberOf(token, page);
        setPosts((prevPosts) => [...prevPosts, ...response]);
        console.log(response);
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    fetchData();
  }, [page]);

  const handleShowMoreClick = () => {
    setPage((prevPage) => prevPage + 1);
  };

  let defaultImageUrl = 'https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg';
  const imageSize = '30px';

  return (
    <>
      <style>
        {`
          .post-card {
            border: 1px solid #ccc;
            transition: border 0.3s ease-in-out, cursor 0.3s ease-in-out;
            box-sizing: border-box; /* Include padding and border in the box dimensions */
          }

          .post-card:hover {
            border: 3px solid black;
            cursor: pointer;
            box-sizing: border-box; /* Include padding and border in the box dimensions */
          }

          .info-line {
            display: flex;
            align-items: center;
            margin-bottom: 5px;  /* Adjusted margin */
          }

          .info-line div {
            margin-right: 5px;  /* Adjusted margin */
          }

          .info-line p {
            margin-bottom: 0;
          }
        `}
      </style>
      <Container>
        <Row>
          {posts.map((post, index) => (
            <div key={index} className="mb-3">
              <Link to={`/community/${post.communityName}/${post.id}`} className="text-decoration-none">
                <Card className="post-card">
                  <Card.Body>
                    {/* Line 1: Round Image for Community */}
                    <div className="info-line">
                      <div>
                        <RoundImage
                          imageUrl={
                            post.pathToCommunityImage
                              ? `http://localhost:8080/image/get_file_by_filename/${post.pathToCommunityImage}`
                              : defaultImageUrl
                          }
                          to={`/community/${post.communityName}/set_logo`}
                          sizeInPixels={imageSize}
                        />
                      </div>
                      <p className="mb-0">{post.communityName}</p>
                      <span className="ms-2">...posted by</span>
                      {/* Round Image for User */}
                      <div>
                        <RoundImage
                          imageUrl={
                            post.pathToUserImage
                              ? `http://localhost:8080/image/get_file_by_filename/${post.pathToUserImage}`
                              : defaultImageUrl
                          }
                          to={`/community/${post.communityName}/set_logo`}
                          sizeInPixels={imageSize}
                        />
                      </div>
                      <p className="mb-0">{post.username}</p>
                    </div>

                    {/* Title */}
                    <Card.Title>{post.title}</Card.Title>

                    {/* Post Image */}
                    {post.pathToPostImage && (
                      <Card.Img
                        variant="top"
                        src={`http://localhost:8080/image/get_file_by_filename/${post.pathToPostImage}`}
                        alt="Post Image"
                      />
                    )}

                    {/* Content */}
                    <Card.Text style={{ whiteSpace: 'pre-line' }}>{post.content}</Card.Text>
                    {/* createdAt */}
                    <Card.Text className="text-muted small">{post.createdAt}</Card.Text>
                  </Card.Body>
                </Card>
              </Link>
            </div>
          ))}
        </Row>
        <Row className="justify-content-center mt-3">
          <Button onClick={handleShowMoreClick}>Show more posts</Button>
        </Row>
      </Container>
    </>
  );
}
