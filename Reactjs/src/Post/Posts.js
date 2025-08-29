import React from 'react';
import { Card, Row } from 'react-bootstrap';
import RoundImage from '../Community/RoundImage';
import { Link } from 'react-router-dom';

export default function Posts({ posts }) {
  const defaultImageUrl = 'https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg';
  const imageSize = '30px';

  const truncateWords = (str, numWords) => {
    const words = str.split(' ');
    if (words.length > numWords) {
      return words.slice(0, numWords).join(' ') + '...';
    }
    return str;
  };

  return (
    <div>
      <style>
        {`
          .post-card {
            border: 1px solid #ccc;
            box-sizing: border-box;
          }

          .post-card:hover {
            border: 3px solid black;
            cursor: pointer;
            box-sizing: border-box;
          }

          .info-line {
            display: flex;
            align-items: center;
            margin-bottom: 5px;
          }

          .info-line div {
            margin-right: 5px;
          }

          .info-line p {
            margin-bottom: 0;
          }

          .post-image {
            max-height: 500px;
            object-fit: cover;
          }
        `}
      </style>
      <Row>
        {posts.map((post, index) => (
          <div key={index} className="mb-3">
            <Link to={`/community/${post.communityName}/${post.id}`} className="text-decoration-none">
              <Card className="post-card">
                <Card.Body>
                  <div className="info-line">
                    <div>
                      <RoundImage
                        imageUrl={
                          post.pathToCommunityImage
                            ? `http://localhost:8080/image/get_file_by_filename/${post.pathToCommunityImage}`
                            : defaultImageUrl
                        }
                        to={`/community/${post.communityName}`}
                        text={"See community"}
                        sizeInPixels={imageSize}
                      />
                    </div>
                    <p className="mb-0">{post.communityName}</p>
                    <span className="ms-2">...posted by</span>
                    <div>
                      <RoundImage
                        imageUrl={
                          post.pathToUserImage
                            ? `http://localhost:8080/image/get_file_by_filename/${post.pathToUserImage}`
                            : defaultImageUrl
                        }
                        to={`/profile/${post.username}`}
                        text={"See profile"}

                        sizeInPixels={imageSize}
                      />


                      
                    </div>
                    <p className="mb-0">{post.username}</p>
                  </div>
                  <Card.Title>{post.title}</Card.Title>
                  {post.pathToPostImage && (
                    <Card.Img
                      className="post-image"
                      variant="top"
                      src={`http://localhost:8080/image/get_file_by_filename/${post.pathToPostImage}`}
                      alt="Post Image"
                    />
                  )}
                  <Card.Text style={{ whiteSpace: 'pre-line' }}>{truncateWords(post.content, 20)}</Card.Text>
                  <Card.Text className="text-muted small">{post.createdAt}</Card.Text>
                </Card.Body>
              </Card>
            </Link>
          </div>
        ))}
      </Row>
    </div>
  );
}
