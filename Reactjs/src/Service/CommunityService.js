
import {createAxiosInstance, request, hasToken, handleRequest} from './utils';


const api = createAxiosInstance();



export const saveCommunity = (communityData) =>
    handleRequest(() => api.post(`/community`, communityData));

export const deleteCommunity = (communityName) =>
    handleRequest(() => api.delete(`/community/${communityName}`));

export const makeUserBecomeMemberOfCommunity = (communityName) =>
    handleRequest(() => api.post(`/community/member/${communityName}`));


export const unsubscribeFromCommunity = (communityName) =>
    handleRequest(() => api.get(`/community/unsubscribe/${communityName}`));

export const banUser = (communityName, username) =>
    handleRequest(() => api.post(`/community/ban/${communityName}/${username}`, null));

export const acceptJoinRequest = (communityName, fromUser) =>
    handleRequest(() => api.post(`/message/acceptRequestToJoinCommunity/${communityName}/${fromUser}`, null));

export const removeModRights = (communityName, usernameToRemoveMod) =>
    handleRequest(() => api.post(`/community/remove_mod/${communityName}/${usernameToRemoveMod}`, null));


export const getRequestsToJoin = (page, communityName) =>
    handleRequest(() => api.get(`/message/getRequestsToJoinCommunity/${page}/${communityName}`));

export const requestToJoinCommunity = (communityName) =>
    handleRequest(() => api.get(`/message/requestToJoinCommunity/${communityName}`));

export const makeUserBecomeMod = (usernameMod, communityName) =>
    handleRequest(() => api.post(`/community/mod/${communityName}/${usernameMod}`, null));

export const makeUserBecomeAdmin = (usernameAdmin, communityName) =>
    handleRequest(() => api.post(`/community/admin/${communityName}/${usernameAdmin}`, null));

export const updateModeratorRights = (communityName, moderatorRights) =>
    handleRequest(() => api.post(`/community/mod_rights/${communityName}`, moderatorRights));


export const getModeratorRights = (communityName) =>
    handleRequest(() => api.get(`community/mod_rights/${communityName}`));


export const getUsers = (communityName) =>
    handleRequest(() => api.get(`/community/users/${communityName}`));


export async function getCommunity(communityName) {
    const link = hasToken()
      ? `/community/${communityName}`
      : `/community/no_logged_in/${communityName}`;
    return await handleRequest(() => api.get(link));
}



export async function setLogo(file, communityName) {
    const formData = new FormData();
    formData.append('file', file);

    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/logo/" + communityName
    return (await request(() => api.post(link, formData))).status;
}


export async function getMembersOfCommunity(communityName) {
    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/members/" + communityName
    return (await request(() => api.get(link))).data;
}


export async function getUserIsHasRoleInCommunityOrIsNotAMember(communityName) {
    if (!hasToken) {
        return {role: "NOT_MEMBER"}
    }
    const link = "http://localhost:8080/community/role/" + communityName
    return await handleRequest(() => api.get(link));
}


export async function setWallpaper(file, communityName) {
    const formData = new FormData();
    formData.append('file', file);

    const api = createAxiosInstance();
    var link = "http://localhost:8080/community/wallpaper/" + communityName
    return (await request(() => api.post(link, formData)));
}
