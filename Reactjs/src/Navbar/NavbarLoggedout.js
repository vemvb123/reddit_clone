import React from 'react'
import { Nav } from 'react-bootstrap';
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';






export default function NavbarLoggedout() {






  return (



 <Navbar className="bg-body-tertiary">
    <Container>
      <Navbar.Brand href="/">
        <img
          src="https://cdn.worldvectorlogo.com/logos/reddit-logo-new.svg"
          width="100"
          className="d-inline-block align-top"
          alt="Reddit logo"
        />
      </Navbar.Brand>
      <Nav.Link href="/login">Log in</Nav.Link>
      <Nav.Link href="/register">Register</Nav.Link>
    </Container>
  </Navbar>




  )
}
