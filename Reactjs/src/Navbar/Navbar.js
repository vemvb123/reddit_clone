import React from 'react'
import Nav from 'react-bootstrap/Nav';
import Form from 'react-bootstrap/Form';
import FormControl from 'react-bootstrap/FormControl';
import Button from 'react-bootstrap/Button';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { useState } from 'react';



export default function Navbar() {


    const [showProfileDropdown, setShowProfileDropdown] = useState(false);

    const handleProfileClick = () => {
      // Toggle the visibility of the profile dropdown
      setShowProfileDropdown(!showProfileDropdown);
    };
  
    return (
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
        <div className="container-fluid">
          {/* Brand/logo */}
          <a className="navbar-brand" href="#">
            Your Logo
          </a>
  
          {/* Search bar */}
          <form
            className="d-flex mx-auto"
            style={{ width: '50%', borderRadius: '20px', background: '#333' }}
          >
            <input
              className="form-control me-2 flex-grow-1 rounded-0"
              type="search"
              placeholder="Search"
              aria-label="Search"
              style={{ borderRadius: '20px', background: '#333', color: '#ddd' }}
            />
            <button className="btn btn-outline-success" type="submit">
              Search
            </button>
          </form>
  
          {/* Profile image as a dropdown */}
          <div className="nav-item dropdown" style={{ cursor: 'pointer' }}>
            <img
              src="https://cdn3.iconfinder.com/data/icons/social-messaging-productivity-6/128/profile-circle2-512.png"
              alt="Profile"
              className="rounded-circle dropdown-toggle"
              data-bs-toggle="dropdown"
              aria-expanded="false"
              style={{ width: '40px', height: '40px', marginLeft: '10px' }}
              onClick={handleProfileClick}
            />
            <ul className={`dropdown-menu ${showProfileDropdown ? 'show' : ''}`}>
            <li>
                <a className="dropdown-item" href="/profile/username">
                  View profile
                </a>
              </li>

              <li>
                <a className="dropdown-item" href="/profile/settings">
                  Settings
                </a>
              </li>

              <li>
                <a className="dropdown-item" href="/profile/logout">
                  Logout
                </a>
              </li>

              <li>
                <a className="dropdown-item" href="/create_community">
                  Create Community
                </a>
              </li>
              


            </ul>
          </div>
        </div>
      </nav>
  );
}
