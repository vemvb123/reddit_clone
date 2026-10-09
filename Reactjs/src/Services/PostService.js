import { createAxiosInstance, request, isNotTokenNon } from './utils';



export async function savePost(postData, communityName) {
  const api = createAxiosInstance();
  var link = `/post/${communityName}`
  return (await request(() => api.post(link, postData))).status;
}



export async function getPostIntervall(communityName, date) {
  const api = createAxiosInstance();
  var link = `/post/${communityName}?date=${date}`;
  return (await request(() => api.get(link))).data;
}




export async function getPostsOfUser(page, username) {  
  const api = createAxiosInstance();  
  var link;

  if (isNotTokenNon()) {
    var link = "/post/user/" + page + "/" + username;
  } else {
    var link = "/post/user/" + page + "/" + username;
  }
  return (await request(() => api.get(link))).data;
}



export async function deletePost(postId)
{
  const api = createAxiosInstance();
  var link = "http://localhost:8080/post/" + postId
  return (await request(() => api.delete(link))).status;
}



export async function setImage(file, postId)
{
  const formData = new FormData();
  formData.append('file', file);

  const api = createAxiosInstance();
  var link = "http://localhost:8080/post/image/" + postId
  return (await request(() => api.post(link, formData))).status;
}






export async function getLatestPosts(token, communityName, page)
{
  const api = createAxiosInstance();
  if (isNotTokenNon()) {
    var link = "http://localhost:8080/post/" + page + "/" + communityName
  } else {
    var link = "http://localhost:8080/post/no_logged_in/" + page + "/" + communityName
  }
  return (await request(() => api.get(link))).data;
}




export async function getPost(postId)
{
  const api = createAxiosInstance();
  if (isNotTokenNon()) {
    var link = "http://localhost:8080/post/" + postId
  } else {
    var link = "http://localhost:8080/post/no_logged_in/" + postId
  }
  return (await request(() => api.get(link))).data;
}



export async function getAllPostsOfPublicCommunities(page)
{
  const api = createAxiosInstance();
  var link = "http://localhost:8080/post/public_communities/" + page
  return (await request(() => api.get(link))).data;
}



export async function getPostsMemberOf(page)
{
  const api = createAxiosInstance();
  var link = "http://localhost:8080/post/communities/" + page
  return (await request(() => api.get(link))).data;
}






