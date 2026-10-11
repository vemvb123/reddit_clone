import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import * as auth from "../../Service/auth-header";
import 'bootstrap/dist/css/bootstrap.min.css';
import { getUserByToken } from '../../Service/UserService';
import { useNavigate } from 'react-router-dom';
import ErrorMessage from "../../Component/SiteMessage/ErrorMessage";


const Login = () => {

    const [error, setError] = useState(null)

    const [loginData, setLoginData] = useState({
        password: "",
        username: ""
    })

    const navigate = useNavigate();

    const handleInput = (e) => {
        const {name, value} = e.target;
        setLoginData((prevData) => ({ ...prevData,
        [name]: value }))
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const authenticateResponse = await auth.authenticate(
            loginData.username,
            loginData.password
        );

        if (authenticateResponse.error !== null) {
            setError(authenticateResponse.error);
            return;
        }

        const userResponse = await getUserByToken();

        if (userResponse.error === null) {
            navigate(`/profile/${userResponse.data.username}`);
        } else {
            setError(userResponse.error);
        }
    };

    if (error) {
        return <ErrorMessage message={error}/>
    }
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