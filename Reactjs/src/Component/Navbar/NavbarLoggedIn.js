import React, { useEffect, useState } from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import * as userService from "../../Service/UserService"

import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap styles
import Messages from './Messages';
import logo from "../../Assets/Reddit-Logo.wine.png"
import "../../App.css"
import { defaultUserImage } from '../../Service/utils';



export default function NavbarLoggedIn() {
    
    const [User, setUser] = useState(null)

    useEffect(() => {
        const fetchData = async () => {
                const user = await userService.getUserByToken()
                if (!user.error) {
                    setUser(user.data)
                }
                else {
                    localStorage.removeItem("token");
                    window.location.href = "/login";
                }
          };
          fetchData();
    }, [])

    if (!User) {
        return null;
    }
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
            <NavDropdown
                align="end"
                id="profile-nav-dropdown"
                title={
                    <img
                        src={
                            User.pathToProfileImage
                            ? `http://localhost:8080/image/${User.pathToProfileImage}`
                            : defaultUserImage(User.username)
                        }
                        alt="Profile"
                        className="profile-navbar-image"
                    />
                }
            >
                <NavDropdown.Item href={`/profile/${User.username}`}>View profile</NavDropdown.Item>
                <NavDropdown.Item href="/">View feed</NavDropdown.Item>
                <NavDropdown.Item href={`/profile/${User.username}/communities`}>View communities</NavDropdown.Item>
                <NavDropdown.Item href={`/profile/${User.username}/set_profile_image`}>Change profile image</NavDropdown.Item>
                <NavDropdown.Item href="#action/3.3">Logout</NavDropdown.Item>
                <NavDropdown.Divider />
                <NavDropdown.Item href="/create_community">Create community</NavDropdown.Item>
            </NavDropdown>

            <h3>{User.username}</h3>
        </div>

      </Container>

    </Navbar>
  )
}
