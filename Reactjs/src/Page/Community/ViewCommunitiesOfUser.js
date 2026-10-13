import React, { useEffect, useState } from 'react';
import * as userService from '../../Service/UserService';
import { Link, useParams } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Card, Container } from 'react-bootstrap';
import '../../App.css';
import { defaultCommunityImage } from '../../Service/utils';

export default function ViewCommunitiesOfUser({ username: propsUsername }) {
  const { username: paramsUsername } = useParams();
  const username = propsUsername || paramsUsername;

  const [communities, setCommunities] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await userService.getCommunitiesUserIsMemberOf(username);
        if (!response.error) {
            setCommunities(response.data);
        }
        console.log(response);
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    fetchData();
    console.log(communities);
  }, [username]);

  return (
    <Container className='mt-5'>
      {communities.map((community) => (
        <Link key={community.title} to={`/community/${community.title}`} className='text-decoration-none'>
          <Card className='community-list-card mb-5 p-2 d-flex flex-row align-items-center gap-3'>
            <img
              src={
                community.communityImage
                  ? `http://localhost:8080/image/${community.communityImage}`
                  : defaultCommunityImage(community.title)
              }
              alt={community.title}
              className='community-list-image'
            />
            <Card.Title className='mb-0'>{community.title}</Card.Title>
          </Card>
        </Link>
      ))}
    </Container>
  );
}
