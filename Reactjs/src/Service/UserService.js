import {createAxiosInstance, request, hasToken, handleRequest} from './utils';


const api = createAxiosInstance();

export const setProfileImage = (file, username) => {
    const formData = new FormData();
    formData.append('file', file);

    return handleRequest(() => api.post(`/user/profile_image/${username}`, formData));
};

export const getUser = (username) =>
    handleRequest(() => api.get(`/user/${username}`));

export const setWallpaper = (file, username) => {
    const formData = new FormData();
    formData.append('file', file);

    return handleRequest(() => api.post(`/user/wallpaper/${username}`, formData));
};

export const getUserByToken = () =>
    handleRequest(() => api.get(`/user/token`));

export const sendFriendRequest = (toUsername) =>
    handleRequest(() => api.post(`/user/send_friend_request/${toUsername}`, null));

export const acceptFriendRequest = (fromUsername) =>
    handleRequest(() => api.post(`/user/accept_friend_request/${fromUsername}`, null));

export const getCommunitiesUserIsMemberOf = (username) =>
    handleRequest(() => api.get(`/user/communities_of_user/${username}`));

export const createChat = (token, toUserCreatingChatWith) =>
    handleRequest(() => api.post(`/api/chat/getChatOrCreateIfAlreadyExists/${toUserCreatingChatWith}`, null));