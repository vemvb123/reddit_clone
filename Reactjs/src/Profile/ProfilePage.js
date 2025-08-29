import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import RoundImage from '../Community/RoundImage';
import * as userService from '../Services/UserService';
import { Button, Nav, Container, Row, Col } from 'react-bootstrap';
import Chat from '../Chat/Chat';
import Wallpaper from '../Community/Wallpaper';
import PostsOfUser from './PostsOfUser';
import CommentsOfUser from './CommentsOfUser';
import ViewCommunitiesOfUser from '../Community/ViewCommunitiesOfUser';

export default function ProfilePage() {

  let params = useParams();
  let username = params.username;


  const token = localStorage.getItem('token');

  const [friendRequestSent, setFriendRequestSent] = useState(false);
  const [profile, setProfile] = useState('');
  const [activeTab, setActiveTab] = useState('posts');
  const [User, setUser] = useState('');
  const [chatId, setChatId] = useState('second'); // Add chatId state

  useEffect(() => {
    const fetchData = async () => {
      try {

        const response = await userService.getUser(username);
        setProfile(response);

        const userData = await userService.getUseByToken(token);
        console.log(userData)
        setUser(userData);
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    fetchData();
  }, [token, username]);

  const sendFriendRequest = async () => {
    if (!friendRequestSent) {
      await userService.sendFriendRequest(token, username);
      setFriendRequestSent(true);
    }
  };

  const createChat = async () => {
    try {
      let response = await userService.createChat(token, username);
      console.log('Chat created:', response);
      setChatId(response);
      return response;
    } catch (error) {
      console.error('Error creating chat:', error);
      throw error; // rethrow the error to propagate it to the caller
    }
  };

  const handleChatClick = async () => {
    let response = await createChat();
    
    // Introduce a delay of 1 second (1000 milliseconds)
      window.location.href = `/chat/${response}/${username}`;
  };

  const defaultImageUrl =
    'https://i.guim.co.uk/img/media/dd703cd39013271a45bc199fae6aa1ddad72faaf/0_0_2000_1200/master/2000.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=2192262d7832a184dbb583c238563695'; // Replace with your default image URL

  return (
    <div>
      <Wallpaper
        imageUrl={`http://localhost:8080/image/get_file_by_filename/${profile.pathToWallpaperImage}`}
        wallpaperIsNull={profile.pathToWallpaperImage == null}
        existingLink={`/profile/${username}`}
      />
      

      <Container>
        <Row className="text-center mt-4">
          <Col className="d-flex">
            <RoundImage
              imageUrl={
                profile.pathToProfileImage
                  ? `http://localhost:8080/image/get_file_by_filename/${profile.pathToProfileImage}`
                  : defaultImageUrl
              }
              to={`/profile/${username}/set_profile_image`}
              sizeInPixels={'200px'}
            />
          </Col>
        </Row>
        <Row>
          <Col>
            <h1>{profile.username}</h1>
          </Col>
        </Row>
        <Row className="text-center mt-3">
          <Col>
            <Nav variant="tabs" defaultActiveKey="/profile" className="justify-content-center">
            {token !== 'non' && User?.username !== profile.username && (
  <Nav.Item>
    <Nav.Link onClick={sendFriendRequest} disabled={friendRequestSent}>
      {friendRequestSent ? 'Friend request sent' : 'Send friend request'}
    </Nav.Link>
  </Nav.Item>
)}
              <Nav.Item>
                <Nav.Link onClick={() => setActiveTab('posts')}>See posts</Nav.Link>
              </Nav.Item>
              <Nav.Item>
                <Nav.Link onClick={() => setActiveTab('comments')}>See comments</Nav.Link>
              </Nav.Item>
              <Nav.Item>
                <Nav.Link onClick={() => setActiveTab('communities')}>See communities</Nav.Link>
              </Nav.Item>
              <Nav.Item>
                <Nav.Link onClick={handleChatClick}>Chat</Nav.Link>
              </Nav.Item>
            </Nav>
          </Col>
        </Row>
      </Container>

      {/* Conditional rendering based on activeTab */}
      {activeTab === 'posts' && <PostsOfUser username={profile.username} />}
      {activeTab === 'comments' && <CommentsOfUser username={profile.username} />}
      {activeTab === 'communities' && <ViewCommunitiesOfUser username={profile.username} />}

    </div>
  );
}
