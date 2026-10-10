

import { createAxiosInstance, request, hasToken } from './utils';



export async function get10LatestMessages(page) {
  const api = createAxiosInstance();
  var link = `http://localhost:8080/message/` + page;
  return (await request(() => api.get(link))).data;
}

export async function deleteMessage(messageId) {
  const api = createAxiosInstance();
  var link = `http://localhost:8080/message/` + messageId;
  return (await request(() => api.delete(link))).data;
}