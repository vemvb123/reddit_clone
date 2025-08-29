import axios from 'axios';


export async function register(firstname, lastname, email, password, username) {
  var link = "http://localhost:8080/auth/register"

  await axios.post(link, {
    firstname: firstname,
    lastname: lastname,
    email: email,
    password: password,
    username: username

  }).then((response) => {
    setToken(response.data.token)
  })
}




export async function authenticate(username, password) {
  var link = "http://localhost:8080/auth/authenticate";
  let responseStatus;

  await axios.post(link, {
    username: username,
    password: password
  })
  .then((response) => {

    setToken(response.data.token) //setting token
    
    responseStatus = response.status


  })
  .catch(error => {
    if (error.response) {
      responseStatus = error.response.status
    }
  });

  return responseStatus
}





export function setToken(token) {
  if (token) {
    localStorage.setItem('token', token)
  }
}



export async function checkValidToken(token)
{
  var link = "http://localhost:8080/auth/validate"
  let responseStatus;

  const authHeader = {
    Authorization: `Bearer ${token}`
  }
  
  await axios.get(link, {headers: authHeader})
  .then(response => {
    responseStatus = response.status
  })
  .catch(error => {
    if (error.response) {
      responseStatus = error.response.status
    }
  });

  return responseStatus
}







  /*
  let API_URL = "http://localhost:8080/test/"

  let token = localStorage.getItem('token');

  const authHeader = {
    Authorization: `Bearer ${token}`
  }







  const getUserContent = () => {
    console.log(token)
    return axios.get(`${API_URL}user`, {headers: authHeader})
    .then((response) => console.log(response.data));
  };



*/