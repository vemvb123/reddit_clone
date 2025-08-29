import axios from 'axios';



export async function setProfileImage(token, file, username)
{
    var link = "http://localhost:8080/user/set_profile_image_for_user/" + username
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



export async function getUser(username)
{
    var link = "http://localhost:8080/user/get_user_by_username/" + username
    let responseStatus;
    let data;


    await axios.get(link)
    .then(response => {
        data = response.data
        responseStatus = response.status
    })
    .catch(error => {
        if (error.response) {
            responseStatus = error.response.status
        }
    });

    return data;
}











export async function setWallpaper(token, file, username)
{
    var link = "http://localhost:8080/user/setWallpaper/" + username
    let responseStatus;
    console.log("isetwallpaper")
    console.log(token)


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









export async function getUseByToken(token)
{
    var link = "http://localhost:8080/user/get_user_by_token"
    let responseStatus;
    let data;

    const config = {
        headers: {
          Authorization: `Bearer ${token}` // Replace with your authorization token
        }
    }

    await axios.get(link, config)
    .then(response => {
        data = response.data
        responseStatus = response.status
    })
    .catch(error => {
        if (error.response) {
            responseStatus = error.response.status
        }
    });

    return data;
}


export async function sendFriendRequest(token, toUsername)
{
    var link = "http://localhost:8080/user/send_friend_request/" + toUsername
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




export async function acceptFriendRequest(token, fromUsername)
{
    var link = "http://localhost:8080/user/accept_friend_request/" + fromUsername
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



export async function getCommunitiesUserIsMemberOf(username)
{
    var link = "http://localhost:8080/user/get_communities_of_user/" + username
    let responseStatus;
    let data;



    await axios.get(link)
    .then(response => {
        data = response.data
        responseStatus = response.status
    })
    .catch(error => {
        if (error.response) {
            responseStatus = error.response.status
        }
    });

    return data;
}



export async function createChat(token, toUserCreatingChatWith)
{
    console.log("herher")
    var link = "http://localhost:8080/api/chat/getChatOrCreateIfAlreadyExists/" + toUserCreatingChatWith
    let responseStatus;
    let data;

    const config = {
        headers: {
          Authorization: `Bearer ${token}` // Replace with your authorization token
        }
    }

    await axios.post(link, null, config)
    .then(response => {
        responseStatus = response.status
        data = response.data
    })
    .catch(error => {
        if (error.response) {
            responseStatus = error.response.status
        }
    });

    return data;
}