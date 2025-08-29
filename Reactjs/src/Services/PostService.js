import axios from 'axios';

export async function savePost(token, postData, communityName)
{
    var link = "http://localhost:8080/post/save_post_to_community/" + communityName
    let responseStatus;

    const config = {
        headers: {
          Authorization: `Bearer ${token}` // Replace with your authorization token
        }
    }

    await axios.post(link, postData, config)
    .then(response => {
        responseStatus = response.status
    })
    .catch(error => {
        if (error.response) {
            responseStatus = error.response.status
        }
    });

    return responseStatus;
}






export async function getPostIntervall(token, communityName, date) {
    let data = [];
    let responseStatus;

    

    var link = `http://localhost:8080/post/get_20_posts_intervall/${communityName}?date=${date}`;

  
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
  



  export async function getPostsOfUser(token, page, username) {
    let data = [];
    let responseStatus;
  
    console.log("hersada")
    console.log(token)
    if (token !== "non") {
      var link = "http://localhost:8080/post/get_20_latets_posts_of_user/" + page + "/" + username;

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
    }
    else {
      var link = "http://localhost:8080/post/get_20_latets_posts_of_user_not_logged_in/" + page + "/" + username;

      
        await axios.get(link)
        .then(response => {
            data = response.data;
        })
        .catch(error => {
          if (error.response) {
              responseStatus = error.response.status
          }
        });
    
    }

  
    return data;
  }


  export async function deletePost(token, postId)
  {
      var link = "http://localhost:8080/post/delete_post_by_id/" + postId
      let responseStatus;
  
      const config = {
          headers: {
            Authorization: `Bearer ${token}` // Replace with your authorization token
          }
      }
  
      await axios.delete(link, config)
      .then(response => {
          responseStatus = response.status
      })
      .catch(error => {
          if (error.response) {
              responseStatus = error.response.status
          }
      });
  
      return responseStatus;
  }









  export async function setImage(token, file, postId)
  {
      var link = "http://localhost:8080/post/setImage/" + postId
      let responseStatus;
  
  
  
      const config = {
          headers: {
            Authorization: `Bearer ${token}` // Replace with your authorization token
          }
      }
  
      const formData = new FormData();
      formData.append('file', file);
  
  
  
      await axios.post(link, formData, config)
      .then(response => {
          responseStatus = response.status
      })
      .catch(error => {
          if (error.response) {
              responseStatus = error.response.status
          }
      });
  
      return responseStatus;
  }






export async function getLatestPosts(token, communityName, page)
{
  let data = [];
  let responseStatus;


  if (token !== "non") {
    var link = "http://localhost:8080/post/get_20_latets_posts_of_community/" + page + "/" + communityName

    const config = {
      headers: {
        Authorization: `Bearer ${token}` // Replace with your authorization token
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
  }
  else {
    console.log("callon")
    var link = "http://localhost:8080/post/get_20_latets_posts_of_community_without_token/" + page + "/" + communityName

  
    await axios.get(link)
    .then(response => {
        data = response.data;
    })
    .catch(error => {
      if (error.response) {
          responseStatus = error.response.status
      }
    });
  }



  return data
}





export async function getPost(token, postId)
{
  let data = [];
  let responseStatus;


  if (token !== "non") {
    var link = "http://localhost:8080/post/get_post/" + postId

    const config = {
      headers: {
        Authorization: `Bearer ${token}` // Replace with your authorization token
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
  }
  else {
    var link = "http://localhost:8080/post/get_post_not_logged_in/" + postId


    await axios.get(link)
    .then(response => {
        data = response.data;
    })
    .catch(error => {
      if (error.response) {
          responseStatus = error.response.status
      }
    });
  }


  return data

}



export async function getAllPostsOfPublicCommunities(page)
{
  var link = "http://localhost:8080/post/get_all_posts_of_public_communities/" + page
  let data = [];
  let responseStatus;



  await axios.get(link)
  .then(response => {
      data = response.data;
  })
  .catch(error => {
    if (error.response) {
        responseStatus = error.response.status
    }
  });

  return data
}













export async function getPostsMemberOf(token, page)
{
  var link = "http://localhost:8080/post/get_posts_from_communities_member_of/" + page
  let data = [];
  let responseStatus;

  const config = {
    headers: {
      Authorization: `Bearer ${token}` // Replace with your authorization token
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

  return data

}






