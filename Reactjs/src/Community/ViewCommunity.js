import React, { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap styles
import ViewPosts from '../Post/ViewPosts';
import { Card, Container, Row, Button } from 'react-bootstrap';

import Wallpaper from './Wallpaper';
import * as communityService from "../Services/CommunityService"
import * as postService from "../Services/PostService"

import RoundImage from './RoundImage';
import Posts from '../Post/Posts';


export default function ViewCommunity() {

  let params = useParams()
  const communityName = params.community_name;

  const [community, setCommunity] = useState("")
  const [role, setRole] = useState("")
  const [posts, setPosts] = useState([])
  const [page, setPage] = useState(0);

  let token = localStorage.getItem('token');

  

  useEffect(() => {
    const fetchData = async () => {
      console.log("wtf")
      try {
        const isMember = await communityService.getUserIsHasRoleInCommunityOrIsNotAMember(token, communityName)
        const response = await communityService.getCommunity(token, communityName)
        
        const postsResponse = await postService.getLatestPosts(token, communityName, page)

        setPosts( postsResponse )
        setCommunity( response )
        setRole( isMember.role )
        console.log("her her response")
        console.log(response)


      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    fetchData();
  }, [page]);

  const handleShowMoreClick = () => {
    setPage((prevPage) => prevPage + 1);
  };




  const becomeMember = () => {
    
        
    communityService.makeUserBecomeMemberOfCommunity(token, communityName)


  };

  const requestToJoinCommunity = () => {
    
        
    communityService.requestToJoinCommunity(token, communityName)


  };








  const deleteCommunity = () => {
    
        
    communityService.deleteCommunity(token, communityName)


  };

  const unsubscribeFromCommunity = () => {
    communityService.unsubscribeFromCommunity(token, communityName)
  
  }


  // gjør forespørsel for å hente posts til community
  // i backend, hvis man får at COmmunity does not exists error, så får man opp det i frontend

  //     <ViewCommunity communityName={communityName} />
  //         <Wallpaper imageUrl={'http://localhost:8080/image/get_file_by_filename/' + community.communityWallpaper} existingLink={"/community/" + communityName} />



  let imageUrl = "https://assets-prd.ignimgs.com/2022/08/17/top25animecharacters-blogroll-1660777571580.jpg"
  
  let defaultImageUrl = "https://i.guim.co.uk/img/media/dd703cd39013271a45bc199fae6aa1ddad72faaf/0_0_2000_1200/master/2000.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=2192262d7832a184dbb583c238563695"


const noHoverBgStyle = `
  .no-hover-bg:hover {
    background-color: transparent !important;
  }
`;

return (
  <div>
    <style>{noHoverBgStyle}</style>

    <Wallpaper imageUrl={`http://localhost:8080/image/get_file_by_filename/${community.communityWallpaper}`} existingLink={`/community/${communityName}`} role={role} />

    <div style={{ textAlign: 'center', margin: '10px' }}>
      {/* TODO: Make buttons seem more like links and show them based on user's role */}
      {["ADMIN", "MOD"].includes(role) && (
        <>
          <Link to={`/community/${community.title}/settings`} className="btn btn-link no-hover-bg">Community settings</Link>
        </>
      )}


    {["ADMIN"].includes(role) && (
        <>
          <Link to={`/community/${community.title}/change_privleges`} className="btn btn-link no-hover-bg">Change privilege settings</Link>
        </>
      )}


      {/* TODO: Centered buttons above ViewPosts */}
      {["ADMIN", "MOD", "MEMBER"].includes(role) && (
        <>
          <Button href={`/community/${communityName}/submit`} className="btn btn-link no-hover-bg">Make post</Button>
          <Button onClick={unsubscribeFromCommunity} className="btn btn-link no-hover-bg">Unsubscribe</Button>
        </>
      )}

      {/* TODO: Show RequestS to join community button based on role and community type */}
      {(role === "NOT_MEMBER") && (community.communityType === "PRIVATE" || community.communityType === "RESTRICTED") && (
        <Button onClick={requestToJoinCommunity} className="btn btn-link no-hover-bg">Request to join community</Button>
      )}

      {/* TODO: Show RequestS to join community button for PUBLIC community */}
      {(role === "NOT_MEMBER") && (community.communityType === "PUBLIC") && (
        <>
          <Button href={`/community/${communityName}/submit`} className="btn btn-link no-hover-bg">Make post</Button>
        </>
      )}

      {(role === "NOT_MEMBER") && (community.communityType !== "PUBLIC") && (
        <>
          <Button href={`/community/${communityName}/requestsToJoin`} className="btn btn-link no-hover-bg">Request to join community</Button>
        </>
      )}

      <Button href={`/community/${communityName}/members`} className="btn btn-link no-hover-bg">See members</Button>


      {["MOD"].includes(role) && (
        <>
          <Button href={`/community/${communityName}/mod_privleges`} className="btn btn-link no-hover-bg">Your mod privleges</Button>
        </>
      )}

      {/* TODO: Show Become a member button for NOT_MEMBER */}
      {(role === "NOT_MEMBER") && (
        <Button onClick={becomeMember} className="btn btn-link no-hover-bg">Become a member</Button>
      )}
    </div>



    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: '5px', marginTop: '20px', marginBottom: '20px' }}>
      {/* ... (existing code) */}
    </div>

    <Container className=''>

<Posts posts={posts}/>

<Row className="justify-content-center mt-3">
<Button onClick={handleShowMoreClick}>Show more posts</Button>
</Row>



</Container>


  </div>
)




}
