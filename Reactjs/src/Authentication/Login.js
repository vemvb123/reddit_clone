import React, { useEffect } from 'react'
import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import * as auth from "../Services/auth-header";
import 'bootstrap/dist/css/bootstrap.min.css';
import { printState, setUser, setUserToken, setUserUsername } from '../Actions/userActions';
import { useDispatch, useSelector } from 'react-redux';
import { getUseByToken } from '../Services/UserService';
import store from '../Slices';
import { useHistory } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';




const Login = () => {
    const [loginData, setLoginData] = useState({
        password: "",
        username: ""
    })
    const user = useSelector(state => state.user);

    let dispatch = useDispatch()

    const handleInput = (e) => {
        e.preventDefault();

        const {name, value} = e.target;
        setLoginData((prevData) => ({ ...prevData,
        [name]: value }))
    }



    const handleSubmit = async (e) => {
      e.preventDefault();
      
      const response = await auth.authenticate(loginData.username, loginData.password);
      if (response === 200) {
        const user = await getUseByToken(localStorage.getItem("token"));
        navigate(`/profile/${user.username}`)
      }

    };
  





    const navigate = useNavigate();

    
  return (



    


    <div>
    <Form className='w-50 mx-auto mt-5'>


      <Form.Group className="mb-3" controlId="username">
        <Form.Control name="username" onChange={handleInput} type="text" placeholder="Username" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="password">
        <Form.Control name="password" onChange={handleInput} type="password" placeholder="Password" />
      </Form.Group>



      <Button onClick={handleSubmit} variant="primary" type="submit">
        Log in
      </Button>
    </Form>



    </div>
  )
}

export default Login