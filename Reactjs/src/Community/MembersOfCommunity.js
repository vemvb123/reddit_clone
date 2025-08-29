import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import * as communityService from '../Services/CommunityService';
import { Container, Card } from 'react-bootstrap';
import RoundImage from './RoundImage';

export default function MembersOfCommunity() {
  let params = useParams();
  const communityName = params.community_name;

  const [members, setMembers] = useState([]);

  let token = localStorage.getItem('token');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await communityService.getMembersOfCommunity(communityName, token);
        console.log('hallo');
        setMembers(response.reverse()); // Reverse the array
        console.log(response);
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    fetchData();
  }, [communityName, token]);

  const getBorderColor = (member) => {
    if (member.isFriendOfUser) {
      return 'green'; // Green border for friends
    } else if (member.role === 'ADMIN') {
      return 'blue'; // Blue border for admins
    } else if (member.role === 'MOD') {
      return 'red'; // Red border for mods
    } else {
      return 'black'; // Default border color
    }
  };

  return (
    <Container>
      {members.map((member) => (
        <Card key={member.username} style={{ border: `2px solid ${getBorderColor(member)}` }} className='mb-5'>
          <RoundImage
            imageUrl={`http://localhost:8080/image/get_file_by_filename/${member.pathToProfileImage}`}
            to={`/profile/${member.username}`}
            sizeInPixels={'80px'}
          />
          <Card.Title>{member.username}</Card.Title>
          <Card.Text>Role: {member.role}</Card.Text>
          {/* Additional member details can be displayed as needed */}
        </Card>
      ))}
    </Container>
  );
}
