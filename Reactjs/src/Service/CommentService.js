import {createAxiosInstance, hasToken, handleRequest} from './utils';


const api = createAxiosInstance();


export const getComment = (commentId) =>
    handleRequest(() => api.get(`/comment/${commentId}`));

export const getCommentsOfUser = (page, username) => {
    const link = hasToken()
        ? `/comment/not_logged_in/${page}/${username}`
        : `/comment/${page}/${username}`;

    return handleRequest(() => api.get(link));
};

export const saveComment = (commentData) =>
    handleRequest(() => api.post(`/comment`, commentData));

export const deleteComment = (commentId) =>
    handleRequest(() => api.delete(`/comment/${commentId}`));

export const getAllCommentsOfPost = (postId) =>
    handleRequest(() => api.get(`/comment/${postId}`));

export const getIntervallOfComments = (postId, fromPostId, parentCommentId) =>
    handleRequest(() => api.get(`/comment/${postId}/${fromPostId}/${parentCommentId}`));

export const setImage = (file, commentId) => {
    const formData = new FormData();
    formData.append('file', file);

    return handleRequest(() => api.post(`/comment/image/${commentId}`, formData));
};