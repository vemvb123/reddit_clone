import React, { useEffect, useState } from 'react';
import * as postService from '../../Service/PostService';
import { Container, Row, Button } from 'react-bootstrap';
import { useParams } from 'react-router-dom';
import Posts from '../Post/Posts';


export default function PostsOfUser(props) {
  // Use props.username if available, otherwise fallback to useParams
  const { username: propUsername } = props;
  const { username: paramUsername } = useParams();
  const username = propUsername || paramUsername;

  const [posts, setPosts] = useState([]);
  const [page, setPage] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
        const response = await postService.getPostsOfUser(page, username);
        if (!response.error) {
          setPosts((prevPosts) => [...prevPosts, ...response.data]);
        }
    };
    fetchData();
  }, [page, username]);

  const handleShowMoreClick = () => {
    setPage((prevPage) => prevPage + 1);
  };

    return (
      <Container className=''>

      <Posts posts={posts}/>
      {posts?.length === 10 && (
          <Row className="justify-content-center mt-3">
              <Button onClick={handleShowMoreClick}>Show more posts</Button>
          </Row>
      )}
      </Container>
    );
  }