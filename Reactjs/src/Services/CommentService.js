import axios from 'axios';
import { isNotTokenNon } from './utils';



export async function getComment(commentId)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/comment/get_comment/" + commentId
    return (await request(() => api.get(link))).data;
}






export async function getCommentsOfUser(page, username)
{
    const api = createAxiosInstance();
    if (isNotTokenNon()) {
        var link = "http://localhost:8080/comment/get_20_latets_comments_of_user_not_logged_in/" + page + "/" + username
    } else {
        var link = "http://localhost:8080/comment/get_20_latets_comments_of_user/" + page + "/" + username
    }
    return (await request(() => api.get(link))).data;
}



export async function saveComment(commentData)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/comment/saveComment"
    return (await request(() => api.post(link, commentData))).status;
}



export async function deleteComment(commentId)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/comment/delete_comment/" + commentId
    return (await request(() => api.post(link, null))).status;
}


export async function getAllCommentsOfPost(postId)
{
    const api = createAxiosInstance();
    var link = "http://localhost:8080/comment/getCommentsOfPost/" + postId;
    return (await request(() => api.get(link))).data;
}



export async function getIntervallOfComments(postId, fromPostId, parentCommentId)
{
    const api = createAxiosInstance();
    if (isNotTokenNon) {
        var link = "http://localhost:8080/comment/getIntervallOfComments/" + postId + "/" + fromPostId + "/" + parentCommentId;
    } else {
        var link = "http://localhost:8080/comment/getIntervallOfCommentsWithoutToken/" + postId + "/" + fromPostId + "/" + parentCommentId;
    }
    return (await request(() => api.get(link))).data;
}



export async function setImage(file, commentId)
{
    const formData = new FormData();
    formData.append('file', file);

    const api = createAxiosInstance();
    var link = "http://localhost:8080/comment/setCommentImage/" + commentId
    return (await request(() => api.post(link, formData))).status;
}

