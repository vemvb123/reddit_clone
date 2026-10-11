import RoundImage from '../../Component/Community/RoundImage';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Card, Container} from 'react-bootstrap';
import React, { useEffect, useState } from 'react'
import * as communityService from "../../Service/CommunityService"


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

    let defaultImageUrl = "https://static.printler.com/cache/c/8/8/e/6/2/c88e62cb33a7b6e20b60af964d362a10883a43a1.jpg"

    return (
        <Container>

        {Users.map((user) => {
        return <Card>
                <RoundImage 
                    imageUrl={
                        user.pathToProfileImage
                        ? `http://localhost:8080/image/${user.pathToProfileImage}`
                        : defaultImageUrl
                    } to={`/profile/${user.username}`} sizeInPixels={"80px"}></RoundImage>
                <Card.Title>{user.username}</Card.Title>
        </Card> 
    })}
    </Container>
  )
}
