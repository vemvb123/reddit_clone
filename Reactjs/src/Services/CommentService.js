import axios from 'axios';



export async function getComment(token, commentId)
{
  var link = "http://localhost:8080/comment/get_comment/" + commentId
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






export async function getCommentsOfUser(token, page, username)
{
  let data = [];
  let responseStatus;


    console.log(token)
    if (token === "non") {

    var link = "http://localhost:8080/comment/get_20_latets_comments_of_user_not_logged_in/" + page + "/" + username



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
else {
    var link = "http://localhost:8080/comment/get_20_latets_comments_of_user/" + page + "/" + username

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





  return data

}




















export async function saveComment(token, commentData)
{
    var link = "http://localhost:8080/comment/saveComment"
    let responseStatus;

    const config = {
        headers: {
          Authorization: `Bearer ${token}` // Replace with your authorization token
        }
    }

    await axios.post(link, commentData, config)
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



export async function deleteComment(token, commentId)
{
    var link = "http://localhost:8080/comment/delete_comment/" + commentId
    let responseStatus;

    const config = {
        headers: {
          Authorization: `Bearer ${token}` // Replace with your authorization token
        }
    }

    await axios.post(link, null, config)
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


export async function getAllCommentsOfPost(postId)
{
    console.log("i th")
    console.log(postId)
    var link = "http://localhost:8080/comment/getCommentsOfPost/" + postId;
    let data = [];

    await axios.get(link)
    .then(response => {
        data = response.data;
    })
  
    return data
}



export async function getIntervallOfComments(token, postId, fromPostId, parentCommentId)
{
    let data = []


    if (token !== "non") {
        var link = "http://localhost:8080/comment/getIntervallOfComments/" + postId + "/" + fromPostId + "/" + parentCommentId;

        const config = {
            headers: {
              Authorization: `Bearer ${token}` // Replace with your authorization token
            }
        }
    
    
        await axios.get(link, config)
        .then(response => {
            data = response.data
        })
        .catch(error => {
            if (error.response) {
                console.log(error.response)
            }
        });
    }
    else {
        var link = "http://localhost:8080/comment/getIntervallOfCommentsWithoutToken/" + postId + "/" + fromPostId + "/" + parentCommentId;

    
    
        await axios.get(link)
        .then(response => {
            data = response.data
        })
        .catch(error => {
            if (error.response) {
                console.log(error.response)
            }
        });
    }


    return data
}

















export async function setImage(token, file, commentId)
{
    var link = "http://localhost:8080/comment/setCommentImage/" + commentId
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