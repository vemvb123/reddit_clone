import { useParams } from 'react-router-dom/dist';
import RoundImage from './RoundImage';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Card, Container} from 'react-bootstrap';
import React, { useEffect, useState } from 'react'
import * as communityService from "../Services/CommunityService"


export default function ListMembersOfCommunity({communityName}) {



    const [Users, setUsers] = useState([])

    useEffect(() => {
        const fetchData = async () => {
            let response = await communityService.getUsers(communityName)
            setUsers(response)  
        }
        fetchData()
    }, [])
    


    let defaultImageUrl = "https://static.printler.com/cache/c/8/8/e/6/2/c88e62cb33a7b6e20b60af964d362a10883a43a1.jpg"


  return (
    <Container>

    {Users.map((user) => {
        return <Card>
                <RoundImage 
                    imageUrl={
                        user.pathToProfileImage
                        ? `http://localhost:8080/image/get_file_by_filename/${user.pathToProfileImage}`
                        : defaultImageUrl
                    } to={`/profile/${user.username}`} sizeInPixels={"80px"}></RoundImage>
                <Card.Title>{user.username}</Card.Title>
        </Card> 
    })}
        









    </Container>
  )
}
