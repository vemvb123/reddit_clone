import RoundImage from '../../Component/Community/RoundImage';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Card, Container} from 'react-bootstrap';
import React, { useEffect, useState } from 'react'
import * as communityService from "../../Service/CommunityService"
import { defaultUserImage } from '../../Service/utils';


export default function ListMembersOfCommunity({communityName}) {

    const [Users, setUsers] = useState([])

    useEffect(() => {
        const fetchData = async () => {
            const response = await communityService.getUsers(communityName)
            if (!response.error)
                setUsers(response.data)
        }
        fetchData()
    }, [])

    return (
        <Container>

        {Users.map((user) => {
        return <Card>
                <RoundImage 
                    imageUrl={
                        user.pathToProfileImage
                        ? `http://localhost:8080/image/${user.pathToProfileImage}`
                        : defaultUserImage(user.username)
                    } to={`/profile/${user.username}`} sizeInPixels={"80px"}></RoundImage>
                <Card.Title>{user.username}</Card.Title>
        </Card> 
    })}
    </Container>
  )
}
