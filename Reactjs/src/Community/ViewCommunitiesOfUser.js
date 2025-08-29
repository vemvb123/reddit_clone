import React, { useEffect, useState } from 'react';
import * as userService from '../Services/UserService';
import { useParams } from 'react-router-dom';
import RoundImage from './RoundImage';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Card, Container } from 'react-bootstrap';

export default function ViewCommunitiesOfUser({ username: propsUsername }) {
  const { username: paramsUsername } = useParams();
  const username = propsUsername || paramsUsername;

  const [communities, setCommunities] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await userService.getCommunitiesUserIsMemberOf(username);
        setCommunities(response);
        console.log(response);
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    fetchData();
    console.log(communities);
  }, [username]);

  let defaultImageUrl =
    'https://static.printler.com/cache/c/8/8/e/6/2/c88e62cb33a7b6e20b60af964d362a10883a43a1.jpg';

  return (
    <Container className='mt-5'>
      {communities.map((community) => (
        <Card key={community.title} className='mb-5 p-2'>
          <RoundImage
            imageUrl={
              community.communityImage
                ? `http://localhost:8080/image/get_file_by_filename/${community.communityImage}`
                : defaultImageUrl
            }
            to={`/community/${community.title}`}
            sizeInPixels={'80px'}
          ></RoundImage>
          <Card.Title>{community.title}</Card.Title>
        </Card>
      ))}
    </Container>
  );
}
