import React, { useEffect } from 'react'
import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import * as auth from "../Services/auth-header";
import 'bootstrap/dist/css/bootstrap.min.css';
import { useDispatch, useSelector } from 'react-redux';
import { getUserByToken } from '../Services/UserService';
import { useNavigate } from 'react-router-dom';


const Login = () => {
    const [loginData, setLoginData] = useState({
        password: "",
        username: ""
    })

    const handleInput = (e) => {
        const {name, value} = e.target;
        setLoginData((prevData) => ({ ...prevData,
        [name]: value }))
    }


    const handleSubmit = async (e) => {
      e.preventDefault();
      
      const response = await auth.authenticate(loginData.username, loginData.password);
      if (response === 200) {
        const user = await getUserByToken();
        navigate(`/profile/${user.username}`)
      }

    };

    const navigate = useNavigate();

  return (
    <div>

    <Form className='w-50 mx-auto mt-5' onSubmit={handleSubmit}>

      <Form.Group className="mb-3" controlId="username">
        <Form.Control name="username" onChange={handleInput} type="text" placeholder="Username" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="password">
        <Form.Control name="password" onChange={handleInput} type="password" placeholder="Password" />
      </Form.Group>

      <Button variant="primary" type="submit">
        Log in
      </Button>
    </Form>

    </div>
  )
}

export default Login