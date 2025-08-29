import axios from 'axios';

export async function saveCommunity(token, communityData)
{
    console.log("i save comm")
    var link = "http://localhost:8080/community/save_community"
    let responseStatus;

    const config = {
        headers: {
          Authorization: `Bearer ${token}` // Replace with your authorization token
        }
    }

    await axios.post(link, communityData, config)
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


export async function deleteCommunity(token, communityName)
{
    console.log("in delete comm")
    var link = "http://localhost:8080/community/delete_community/" + communityName
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



export async function makeUserBecomeMemberOfCommunity(token, communityName)
{
    var link = "http://localhost:8080/community/makeUserBecomeMember/" + communityName
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




export async function unsubscribeFromCommunity(token, communityName)
{
    var link = "http://localhost:8080/community/unsubscribeFromCommunity/" + communityName 
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


export async function banUser(token, communityName, username)
{
    var link = "http://localhost:8080/community/banUserFromCommunity/" + communityName + "/" + username
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




export async function acceptJoinRequest(token, communityName, fromUser)
{
    var link = "http://localhost:8080/message/acceptRequestToJoinCommunity/" + communityName + "/" +fromUser
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


export async function removeModRights(token, communityName, usernameToRemoveMod)
{
    var link = "http://localhost:8080/community/remove_moderator_rights/" + communityName + "/" + usernameToRemoveMod
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

export async function getRequestsToJoin(token, page, communityName)
{
    var link = "http://localhost:8080/message/getRequestsToJoinCommunity/" + page + "/" + communityName
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





export async function requestToJoinCommunity(token, communityName)
{
    var link = "http://localhost:8080/message/requestToJoinCommunity/" + communityName 
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





export async function makeUserBecomeMod(token, usernameMod, communityName)
{
    var link = "http://localhost:8080/community/makeUserBecomeMod/" + communityName + "/" + usernameMod
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



export async function makeUserBecomeAdmin(token, usernameAdmin, communityName)
{
    var link = "http://localhost:8080/community/makeUserAdmin/" + communityName + "/" + usernameAdmin
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




export async function updateModeratorRights(token, communityName, moderatorRights)
{
    var link = "http://localhost:8080/community/change_moderator_rights/" + communityName
    let responseStatus;

    const config = {
        headers: {
          Authorization: `Bearer ${token}` // Replace with your authorization token
        }
    }

    await axios.post(link, moderatorRights, config)
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


export async function getModeratorRights(token, communityName)
{
    var link = "http://localhost:8080/community/get_moderator_rights/" + communityName
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




export async function getUsers(communityName)
{
    var link = "http://localhost:8080/community/getUsers/" + communityName
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




export async function getCommunity(token, communityName)
{
    let responseStatus;
    let data;


    if (token !== "non") {
        var link = "http://localhost:8080/community/get_community_by_name/" + communityName

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
    
    } else {
        var link = "http://localhost:8080/community/get_community_by_name_without_token/" + communityName

    
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
    }







    return data;
}



export async function setLogo(token, file, communityName)
{
    var link = "http://localhost:8080/community/setLogoForCommunity/" + communityName
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


export async function getMembersOfCommunity(communityName, token)
{
    
    var link = "http://localhost:8080/community/get_members/" + communityName

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


export async function getUserIsHasRoleInCommunityOrIsNotAMember(token, communityName)
{
    if (token === "non") {
        return {role: "NON_MEMBER"}
    }
    var link = "http://localhost:8080/community/user_has_role_in_community/" + communityName

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






export async function setWallpaper(token, file, communityName)
{
    var link = "http://localhost:8080/community/setWallpaperForCommunity/" + communityName
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







