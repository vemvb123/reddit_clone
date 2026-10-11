
import React, { useEffect, useState } from 'react';
import * as postService from '../../Service/PostService';
import { Card, Container, Row, Button } from 'react-bootstrap';
import RoundImage from '../../Component/Community/RoundImage';
import { Link } from 'react-router-dom';
import ViewPosts from '../../Component/Post/ViewPosts';
import Posts from '../../Component/Post/Posts';



export default function ViewAllPublicCommunityPosts() {

      const [posts, setPosts] = useState([]);
      const [page, setPage] = useState(0);

      useEffect(() => {
          const fetchData = async () => {
              try {
                  const response = await postService.getAllPostsOfPublicCommunities(page);
                  if (response.status === 200) {
                      setPosts((prevPosts) => [...prevPosts, ...response.data]);
                  } else {
                      console.error(response)
                  }
              } catch (error) {
                  console.log('Error fetching posts:', error);
              }
          };

          fetchData();
        }, [page]);

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
