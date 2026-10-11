import React, { useEffect, useState } from 'react';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { useParams } from 'react-router-dom';
import * as communityService from '../../Service/CommunityService'


const capitalizeFirstLetter = (str) => {
    return str.charAt(0).toUpperCase() + str.slice(1);
  };

export default function ChangePrivlegdeSettings() {
    let params = useParams();
    const communityName = params.community_name;

    const [usernameAdmin, setUsernameAdmin] = useState(null);
    const [usernameMod, setUsernameMod] = useState(null);
    const [usernameToRemoveMod, setUsernameToRemoveMod] = useState(null);

    const [moderatorRights, setModeratorRights] = useState({
        banUsers: false,
        changeCommunityImage: false,
        changeWallpaper: false,
        deleteCommunity: false,
        deleteOthersComments: false,
        deleteOthersPosts: false,
        makeAnnouncements: false,
        changeCommunityDescription: false,
    });


    useEffect(() => {
        const fetchData = async () => {
            const response = await communityService.getModeratorRights(communityName);
            if (!response.error) {
                setModeratorRights(response.data);
            }
        };
        fetchData();
    }, [communityName]);


  const handleUpdateRights = () => {
        communityService.updateModeratorRights(communityName, moderatorRights)
  };

  const handleInputChangeMod = (e) => {
        setUsernameMod(e.target.value);
  };

  const makeUserBecomeMod = (e) => {
    communityService.makeUserBecomeMod(usernameMod, communityName)
  };

  const handleInputChangeAdmin = (e) => {
    setUsernameAdmin(e.target.value);
  };

  const makeUserBecomeAdmin = () => {
    communityService.makeUserBecomeAdmin(usernameAdmin, communityName)
  };

  const handleInputChangeRemoveMod = (e) => {
    setUsernameToRemoveMod(e.target.value);
  };

const removeModeratorRights = (e) => {
  communityService.removeModRights(communityName, usernameToRemoveMod)
};


  return (
    <>
    <p>{communityName}</p>
    <Card style={{ width: '30rem' }}>
      <Card.Body>
        <Card.Title>Moderator has the right to...</Card.Title>
        <Form>
          {Object.entries(moderatorRights).map(([right, value]) => (
            <Form.Check
              key={right}
              type="checkbox"
              label={capitalizeFirstLetter(right.replace(/([A-Z])/g, ' $1').trim())} // Capitalize the first letter of each word
              checked={value}
              onChange={() => setModeratorRights((prevRights) => ({ ...prevRights, [right]: !prevRights[right] }))}
            />
          ))}
          <Button variant="primary" onClick={handleUpdateRights}>
            Update moderator rights
          </Button>
        </Form>
      </Card.Body>
    </Card>



    <Card>
    <Form onSubmit={makeUserBecomeMod}>
          <Form.Group controlId="usernameMod">
            <Form.Label>Write the name of the user to become a moderator</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter username"
              value={usernameMod}
              onChange={handleInputChangeMod}
            />
          </Form.Group>
          <Button variant="primary" type="submit">
            Submit
          </Button>
        </Form>
    </Card>


    <Card>
    <Form onSubmit={makeUserBecomeAdmin}>
          <Form.Group controlId="usernameAdmin">
            <Form.Label>Write the name of the user to become an administrator</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter username"
              value={usernameAdmin}
              onChange={handleInputChangeAdmin}
            />
          </Form.Group>
          <Button variant="primary" type="submit">
            Submit
          </Button>
        </Form>
    </Card>

    <Card>
    <Form onSubmit={removeModeratorRights}>
          <Form.Group controlId="usernameToRemoveMod">
            <Form.Label>Write the name of the user to remove mod rights from</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter username"
              value={usernameToRemoveMod}
              onChange={handleInputChangeRemoveMod}
            />
          </Form.Group>
          <Button variant="primary" type="submit">
            Submit
          </Button>
        </Form>
    </Card>



    </>
  )
}
