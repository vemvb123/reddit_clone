

import {createAxiosInstance, handleRequest} from './utils';


const api = createAxiosInstance();

export const get10LatestMessages = (page) =>
    handleRequest(() => api.get(`/message/${page}`));

export const deleteMessage = (messageId) =>
    handleRequest(() => api.delete(`/message/${messageId}`));
