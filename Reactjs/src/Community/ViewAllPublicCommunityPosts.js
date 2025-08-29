
import React, { useEffect, useState } from 'react';
import * as postService from '../Services/PostService';
import { Card, Container, Row, Button } from 'react-bootstrap';
import RoundImage from './RoundImage';
import { Link } from 'react-router-dom';
import ViewPosts from '../Post/ViewPosts';
import Posts from '../Post/Posts';



export default function ViewAllPublicCommunityPosts() {






    
      const [posts, setPosts] = useState([]);
      const [page, setPage] = useState(0);
    
      useEffect(() => {
        const fetchData = async () => {
          try {
            const token = localStorage.getItem('token');
            const response = await postService.getAllPostsOfPublicCommunities(page);
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
    

    
      return (

                            
 
        <Container className=''>

            <Posts posts={posts}/>

          
          <Row className="justify-content-center mt-3">
            <Button onClick={handleShowMoreClick}>Show more posts</Button>
          </Row>


          
        </Container>



    );
  }
  

    