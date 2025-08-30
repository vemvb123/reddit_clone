import axios from 'axios';
import { isNotTokenNon } from './utils';

export async function saveCommunity(communityData)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/save_community"
    return (await request(() => api.post(link, communityData))).status;
}



export async function deleteCommunity(communityName)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/delete_community/" + communityName
    return (await request(() => api.delete(link))).status;
}



export async function makeUserBecomeMemberOfCommunity(communityName)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/makeUserBecomeMember/" + communityName
    return (await request(() => api.post(link, null))).status;
}



export async function unsubscribeFromCommunity(communityName)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/unsubscribeFromCommunity/" + communityName 
    return (await request(() => api.post(link, null))).status;
}


export async function banUser(communityName, username)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/banUserFromCommunity/" + communityName + "/" + username
    return (await request(() => api.post(link, null))).status;
}




export async function acceptJoinRequest(communityName, fromUser)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/message/acceptRequestToJoinCommunity/" + communityName + "/" +fromUser
    return (await request(() => api.post(link, null))).status;
}



export async function removeModRights(communityName, usernameToRemoveMod)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/remove_moderator_rights/" + communityName + "/" + usernameToRemoveMod
    return (await request(() => api.post(link, null))).status;
}



export async function getRequestsToJoin(page, communityName)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/message/getRequestsToJoinCommunity/" + page + "/" + communityName
    return (await request(() => api.get(link))).data;
}





export async function requestToJoinCommunity(communityName)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/message/requestToJoinCommunity/" + communityName 
    return (await request(() => api.get(link, null))).status;
}



export async function makeUserBecomeMod(usernameMod, communityName)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/makeUserBecomeMod/" + communityName + "/" + usernameMod
    return (await request(() => api.post(link, null))).status;
}



export async function makeUserBecomeAdmin(usernameAdmin, communityName)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/makeUserAdmin/" + communityName + "/" + usernameAdmin
    return (await request(() => api.post(link, null))).status;
}




export async function updateModeratorRights(communityName, moderatorRights)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/change_moderator_rights/" + communityName
    return (await request(() => api.post(link, moderatorRights))).status;
}



export async function getModeratorRights(communityName)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/get_moderator_rights/" + communityName
    return (await request(() => api.get(link))).status;
}




export async function getUsers(communityName)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/getUsers/" + communityName
    return (await request(() => api.get(link))).status;
}




export async function getCommunity(communityName)
{
    const api = createAxiosInstance();
    if (isNotTokenNon()) {
        var link = "http://localhost:8080/community/get_community_by_name/" + communityName

    } else {
        var link = "http://localhost:8080/community/get_community_by_name_without_token/" + communityName

    }
    return (await request(() => api.get(link))).data;
}



export async function setLogo(file, communityName)
{
    const formData = new FormData();
    formData.append('file', file);

    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/setLogoForCommunity/" + communityName
    return (await request(() => api.post(link, formData))).status;
}


export async function getMembersOfCommunity(communityName)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/get_members/" + communityName
    return (await request(() => api.get(link))).data;
}


export async function getUserIsHasRoleInCommunityOrIsNotAMember(communityName)
{
    if (isNotTokenNon) {
        return {role: "NON_MEMBER"}
    }
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/user_has_role_in_community/" + communityName
    return (await request(() => api.get(link))).data;
}






export async function setWallpaper(file, communityName)
{
    const formData = new FormData();
    formData.append('file', file);

    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/setWallpaperForCommunity/" + communityName
    return (await request(() => api.post(link, formData))).status;
}







