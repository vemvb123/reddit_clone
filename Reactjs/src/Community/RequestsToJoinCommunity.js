import React, { useEffect, useState } from 'react'
import { Card, Container, Row, Button } from 'react-bootstrap';
import RoundImage from './RoundImage';
import { Link } from 'react-router-dom';

import * as communityService from "../Services/CommunityService"
import { useParams } from 'react-router-dom'
import { Comments } from '../Comment/Comments';


export default function RequestsToJoinCommunity() {

    let params = useParams()
    let communityName = params.community_name

    let token = localStorage.getItem("token")


    const [RequestsToJoin, setRequestsToJoin] = useState([])


    const getRequestsToJoin = async () => {
        const response = await communityService.getRequestsToJoin(token, page, communityName)
        console.log(response + "herda")
        setRequestsToJoin(response)
    }



    useEffect(() => {
      getRequestsToJoin()
     

    }, [])
    
  
    const acceptJoinRequest = (fromUser) => {
        communityService.acceptJoinRequest(token, communityName, fromUser)
    }
  
  
    const [page, setPage] = useState(0);
    const handleShowMoreClick = () => {
        setPage((prevPage) => prevPage + 1);
      };
      <Button onClick={handleShowMoreClick}>Show more requests</Button>

  
  
  
  
  
  
  
  
    return (
    <div>




        {RequestsToJoin.map((RequestToJoin, index) => 
            <div>
            <p>{RequestToJoin.content}</p>
            <Button onClick={acceptJoinRequest(RequestToJoin.fromUsername)}>Accept request</Button>


            </div>
        
        )}




    </div>
  )
}
