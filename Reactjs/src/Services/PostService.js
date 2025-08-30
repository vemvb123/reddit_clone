import { createAxiosInstance, request, isNotTokenNon } from './utils';



export async function savePost(postData, communityName) {
  const api = createAxiosInstance();
  var link = `/post/save_post_to_community/${communityName}`
  return (await request(() => api.post(link, postData))).status;
}



export async function getPostIntervall(communityName, date) {
  const api = createAxiosInstance();
  var link = `/post/get_20_posts_intervall/${communityName}?date=${date}`;
  return (await request(() => api.get(link))).data;
}




export async function getPostsOfUser(page, username) {  
  const api = createAxiosInstance();  
  var link;

  if (isNotTokenNon()) {
    var link = "/post/get_20_latets_posts_of_user/" + page + "/" + username;
  } else {
    var link = "/post/get_20_latets_posts_of_user_not_logged_in/" + page + "/" + username;
  }
  return (await request(() => api.get(link))).data;
}



export async function deletePost(postId)
{
  const api = createAxiosInstance();
  var link = "http://localhost:8080/post/delete_post_by_id/" + postId
  return (await request(() => api.delete(link))).status;
}



export async function setImage(file, postId)
{
  const formData = new FormData();
  formData.append('file', file);

  const api = createAxiosInstance();
  var link = "http://localhost:8080/post/setImage/" + postId
  return (await request(() => api.post(link, formData))).status;
}






export async function getLatestPosts(token, communityName, page)
{
  const api = createAxiosInstance();
  if (isNotTokenNon()) {
    var link = "http://localhost:8080/post/get_20_latets_posts_of_community/" + page + "/" + communityName
  } else {
    var link = "http://localhost:8080/post/get_20_latets_posts_of_community_without_token/" + page + "/" + communityName
  }
  return (await request(() => api.get(link))).data;
}




export async function getPost(postId)
{
  const api = createAxiosInstance();
  if (isNotTokenNon()) {
    var link = "http://localhost:8080/post/get_post/" + postId
  } else {
    var link = "http://localhost:8080/post/get_post_not_logged_in/" + postId
  }
  return (await request(() => api.get(link))).data;
}



export async function getAllPostsOfPublicCommunities(page)
{
  const api = createAxiosInstance();
  var link = "http://localhost:8080/post/get_all_posts_of_public_communities/" + page
  return (await request(() => api.get(link))).data;
}



export async function getPostsMemberOf(page)
{
  const api = createAxiosInstance();
  var link = "http://localhost:8080/post/get_posts_from_communities_member_of/" + page
  return (await request(() => api.get(link))).data;
}






