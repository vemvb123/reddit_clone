import axios from 'axios';



export async function setProfileImage(file, username)
{
    const formData = new FormData();
    formData.append('file', file);

    const api = createAxiosInstance();
    var link = "http://localhost:8080/user/set_profile_image_for_user/" + username
    return (await request(() => api.post(link, formData)).status);
}



export async function getUser(username)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/user/get_user_by_username/" + username
    return (await request(() => api.get(link)).data);
}


export async function setWallpaper(file, username)
{
    const formData = new FormData();
    formData.append('file', file);

    const api = createAxiosInstance();
    var link = "http://localhost:8080/user/setWallpaper/" + username
    return (await request(() => api.get(link, formData)).status);
}


export async function getUseByToken()
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/user/get_user_by_token"
    return (await request(() => api.get(link)).data);
}


export async function sendFriendRequest(toUsername)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/user/send_friend_request/" + toUsername
    return (await request(() => api.post(link, null)).status);
}




export async function acceptFriendRequest(fromUsername)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/user/accept_friend_request/" + fromUsername
    return (await request(() => api.post(link, null)).status);
}



export async function getCommunitiesUserIsMemberOf(username)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/user/get_communities_of_user/" + username
    return (await request(() => api.get(link)).data);
}



export async function createChat(token, toUserCreatingChatWith)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/api/chat/getChatOrCreateIfAlreadyExists/" + toUserCreatingChatWith
    return (await request(() => api.post(link, null)).data);
}