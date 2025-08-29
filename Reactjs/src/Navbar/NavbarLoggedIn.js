import React, { useEffect, useState, useContext } from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import * as userService from "../Services/UserService"
import Card from 'react-bootstrap/Card';
import RoundImage from '../Community/RoundImage';

import { useParams, Link } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap styles
import Messages from './Messages';
import { useSelector } from 'react-redux';
import Wallpaper from '../Community/Wallpaper';
import logo from "../Assets/Reddit-Logo.wine.png"

export default function NavbarLoggedIn() {

    
    const [User, setUser] = useState("")

    useEffect(() => {

      console.log()

        const fetchData = async () => {
            try {

              let token = localStorage.getItem('token');
              const response = await userService.getUseByToken(token)

              setUser(response)
            } catch (error) {
              console.error('Error fetching posts:', error);
            }

          };
          fetchData();

    
    }, [])
    



    let defaultImageUrl = "https://static.printler.com/cache/c/8/8/e/6/2/c88e62cb33a7b6e20b60af964d362a10883a43a1.jpg"


  return (
    <Navbar collapseOnSelect expand="lg" className="bg-body-tertiary">



      <Container>
        <Navbar.Brand href="/">

        <img
          src={logo}
          height="100"
          className="d-inline-block align-top"
          alt="Reddit logo"
        />


        </Navbar.Brand>
        <Navbar.Toggle aria-controls="responsive-navbar-nav" />
        <Navbar.Collapse id="responsive-navbar-nav">
      


          <Nav className="me-auto">



          

            <NavDropdown title="Options" id="collapsible-nav-dropdown">
              <NavDropdown.Item href={`/profile/${User.username}`}>View profile</NavDropdown.Item>
              <NavDropdown.Item href="/">View feed</NavDropdown.Item>
              <NavDropdown.Item href={`/profile/${User.username}/communities`}>View communities</NavDropdown.Item>
              <NavDropdown.Item href="#action/3.3">Logout</NavDropdown.Item>


              <NavDropdown.Divider />

              <NavDropdown.Item href="/create_community">Create community</NavDropdown.Item>
            </NavDropdown>
            <Messages User={User}/>

          </Nav>

        </Navbar.Collapse>








    


        <div
            style={{
                display: 'flex',
                flexDirection: 'row', // Stack items vertically
                alignItems: 'center',
                justifyContent: 'center',
                gap: '5px', // Space between image and text
                marginTop: '20px', // Adjust top margin as needed
                marginBottom: '20px', // Adjust bottom margin as needed
            }}    
        >
            <RoundImage
                imageUrl={
                    User.pathToProfileImage
                    ? `http://localhost:8080/image/get_file_by_filename/${User.pathToProfileImage}`
                    : defaultImageUrl
                }
                to={`/profile/${User.username}/set_profile_image`}
                sizeInPixels={"75px"}
            />

            <h3>{User.username}</h3>
        </div>






      </Container>




    </Navbar>
  )
}
