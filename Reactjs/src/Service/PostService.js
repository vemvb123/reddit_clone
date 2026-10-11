import {createAxiosInstance, request, hasToken, handleRequest} from './utils';


const api = createAxiosInstance();


export const savePost = (postData, communityName) =>
    handleRequest(() => api.post(`/post/${communityName}`, postData));

export const getPostIntervall = (communityName, date) =>
    handleRequest(() => api.get(`/post/${communityName}?date=${date}`));

export const getPostsOfUser = (page, username) =>
    handleRequest(() => api.get(`/post/user/${page}/${username}`));

export const deletePost = (postId) =>
    handleRequest(() => api.delete(`/post/${postId}`));

export const setImage = (file, postId) => {
    const formData = new FormData();
    formData.append('file', file);

    return handleRequest(() => api.post(`/post/image/${postId}`, formData));
};

export async function getLatestPosts(communityName, page) {
  const link = hasToken()
      ? `http://localhost:8080/post/${page}/${communityName}`
      : `http://localhost:8080/post/no_logged_in/${page}/${communityName}`
  return await handleRequest(() => api.get(link));
}


export async function getPost(postId) {
  const link = hasToken()
      ? `http://localhost:8080/post/${postId}`
      : `http://localhost:8080/post/no_logged_in/${postId}`
  return await handleRequest(() => api.get(link));
}

export const getAllPostsOfPublicCommunities = (page) =>
    handleRequest(() => api.get(`/post/public_communities/${page}`));

export const getPostsMemberOf = (page) =>
    handleRequest(() => api.get(`/post/communities/${page}`));



