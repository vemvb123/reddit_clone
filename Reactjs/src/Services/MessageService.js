
import axios from 'axios';



export async function get10LatestMessages(token, page) {
    var link = `http://localhost:8080/message/get10MessageToUser/` + page;
    let data = [];
    let responseStatus;
  
    const config = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  
    await axios.get(link, config)
    .then(response => {
        data = response.data;
    })
    .catch(error => {
      if (error.response) {
          responseStatus = error.response.status
      }
    });
  
    return data;
  }







  export async function deleteMessage(token, messageId) {
    var link = `http://localhost:8080/message/deleteMessage/` + messageId;
    let data = [];
    let responseStatus;
  
    const config = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  
    await axios.delete(link, config)
    .then(response => {
        data = response.data;
    })
    .catch(error => {
      if (error.response) {
          responseStatus = error.response.status
      }
    });
  
    return data;
  }