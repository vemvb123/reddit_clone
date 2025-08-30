
import axios from 'axios';



export async function get10LatestMessages(page) {
  const api = createAxiosInstance();
  var link = `http://localhost:8080/message/get10MessageToUser/` + page;
  return (await request(() => api.get(link))).data;
}







export async function deleteMessage(messageId) {
  const api = createAxiosInstance();
  var link = `http://localhost:8080/message/deleteMessage/` + messageId;
  return (await request(() => api.delete(link))).data;
}