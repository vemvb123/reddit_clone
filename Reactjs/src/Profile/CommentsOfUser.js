import React, { useEffect, useState } from 'react';
import * as commentService from '../Services/CommentService';
import { Card, Container, Row, Button } from 'react-bootstrap';
import RoundImage from "../Community/RoundImage";
import { Link, useParams } from 'react-router-dom';

export default function CommentsOfUser({ username: propUsername }) {
    const [comments, setComments] = useState([]);
    const [page, setPage] = useState(0);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await commentService.getCommentsOfUser(token, page, propUsername)
                setComments((prevComments) => [...prevComments, ...response]);
                console.log(response);
            } catch (error) {
                console.error('Error fetching comments:', error);
            }
        };

        fetchData();
    }, [page, propUsername]);

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
                    {comments.map((comment, index) => (
                        <div key={index} className="mb-3">
                            <Link to={`/community/${comment.communityName}/${comment.postId}`} className="text-decoration-none">
                                <Card className="post-card">
                                    <Card.Body>
                                        {/* Line 1: Round Image for Community */}
                                        <div className="info-line">

                                            <div>
                                                <RoundImage
                                                    imageUrl={
                                                        comment.pathToCommunityImage
                                                            ? `http://localhost:8080/image/get_file_by_filename/${comment.pathToCommunityImage}`
                                                            : defaultImageUrl
                                                    }
                                                    to={`/community/${comment.communityName}/set_logo`}
                                                    sizeInPixels={imageSize}
                                                />
                                            </div>
                                            <p className="mb-0">{comment.communityName}</p>
                                            <span className="ms-2">...posted by</span>

                                            {/* Round Image for User */}
                                            <div>
                                                <RoundImage
                                                    imageUrl={
                                                        comment.pathToUserImage
                                                            ? `http://localhost:8080/image/get_file_by_filename/${comment.pathToUserImage}`
                                                            : defaultImageUrl
                                                    }
                                                    to={`/community/${comment.communityName}/set_logo`}
                                                    sizeInPixels={imageSize}
                                                />
                                            </div>
                                            <p className="mb-0">{comment.username}</p>
                                            <span className="ms-2">...replying to</span>

                                            {/* Round Image for user replied to */}
                                            <div>
                                                <RoundImage
                                                    imageUrl={
                                                        comment.usernameRepliedToPathToImage
                                                            ? `http://localhost:8080/image/get_file_by_filename/${comment.usernameRepliedToPathToImage}`
                                                            : defaultImageUrl
                                                    }
                                                    to={`/community/${comment.communityName}/set_logo`}
                                                    sizeInPixels={imageSize}
                                                />
                                            </div>
                                            <p className="mb-0">{comment.usernameRepliedTo}</p>
                                        </div>

                                        {/* Title */}
                                        <Card.Title>{comment.title}</Card.Title>

                                        {/* Post Image */}
                                        {comment.pathToImage && (
                                            <Card.Img
                                                variant="top"
                                                src={`http://localhost:8080/image/get_file_by_filename/${comment.pathToImage}`}
                                                alt="Post Image"
                                            />
                                        )}

                                        {/* Content */}
                                        <Card.Text style={{ whiteSpace: 'pre-line' }}>{comment.description}</Card.Text>
                                        {/* createdAt */}
                                        <Card.Text className="text-muted small">{comment.createdAt}</Card.Text>
                                    </Card.Body>
                                </Card>
                            </Link>
                        </div>
                    ))}
                </Row>
                <Row className="justify-content-center mt-3">
                    <Button onClick={handleShowMoreClick}>Show more comments</Button>
                </Row>
            </Container>
        </>
    );
}
