import React, { useEffect, useState } from 'react';
import * as communityService from "../Services/CommunityService";
import { useParams } from 'react-router-dom';
import { Table, Container } from 'react-bootstrap';

export default function ViewModPrivleges() {
  let params = useParams()
  const communityName = params.community_name;

  const [community, setCommunity] = useState({})

  let token = localStorage.getItem('token');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await communityService.getCommunity(token, communityName)
        setCommunity(response)
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    fetchData();
  }, [communityName, token]);

  const formatPermission = (permission) => {
    // Convert camelCase to space-separated words and capitalize each word
    return permission
      .replace(/([a-z])([A-Z])/g, '$1 $2')
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  };

  const moderatorRights = [
    "moderatorCanBanUser",
    "moderatorCanChangeCommunityDescription",
    "moderatorCanChangeCommunityImage",
    "moderatorCanChangeWallpaper",
    "moderatorCanDeleteCommunity",
    "moderatorCanDeleteOthersComments",
    "moderatorCanDeleteOthersPosts",
    "moderatorCanMakeAnnouncement"
  ];

  return (
    <Container>
      {community && (
        <>
          <h1>{`Moderator rights on ${community.title || 'Community'}`}</h1>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>Permission</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {moderatorRights.map((right) => (
                <tr key={right}>
                  <td>{formatPermission(right)}</td>
                  <td style={{ color: community[right] ? 'green' : 'red' }}>
                    {community[right] ? 'Allowed' : 'Not Allowed'}
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </>
      )}
    </Container>
  );
}
