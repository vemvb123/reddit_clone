import React from 'react'
import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import * as auth from "../../Service/auth-header";
import 'bootstrap/dist/css/bootstrap.min.css';
import {useNavigate} from "react-router-dom";
import {getUser} from "../../Service/UserService";
import LoadingMessage from "../../Component/SiteMessage/LoadingMessage";



const Register = () => {


    const [registerData, setRegisterData] = useState({
        firstname: "",
        lastname: "",
        email: "",
        password: "",
        username: ""
    })

    const [loading, setLoading] = useState(null)

    const navigate = useNavigate();

    const handleInput = (e) => {
        const {name, value} = e.target;
        setRegisterData((prevData) => ({
            ...prevData,
            [name]: value
        }))
    }


    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true)
        const responseStatus = await auth.register(registerData.firstname, registerData.lastname, registerData.email, registerData.password, registerData.username)
        if (responseStatus === 200) {
            await new Promise(resolve => setTimeout(resolve, 5000));
            navigate(`/profile/${registerData.username}`)
        }
    }


    if (loading) {
        return <LoadingMessage/>
    }

  return (

    <div>
    <Form className='w-50 mx-auto mt-5' onSubmit={handleSubmit}>
      <Form.Group className="mb-3" controlId="formBasicEmail">
        <Form.Control name='firstname' onChange={handleInput} type="text" placeholder="First name" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="formBasicPassword">
        <Form.Control name="lastname" onChange={handleInput} type="text" placeholder="Last name" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="formBasicPassword">
        <Form.Control name="email" onChange={handleInput} type="email" placeholder="Email" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="formBasicPassword">
        <Form.Control name="password" onChange={handleInput} type="password" placeholder="Password" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="formBasicPassword">
        <Form.Control name="username" onChange={handleInput} type="text" placeholder="Username" />
      </Form.Group>

      <Button variant="primary" type="submit">
        Register
      </Button>
    </Form>



    </div>
  )
}

export default Register