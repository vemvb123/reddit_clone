import axios from 'axios';


export function createAxiosInstance() {
    let token = localStorage.getItem("token");
  
    return axios.create({
      baseURL: "http://localhost:8080",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
    });
  }


  export async function request(apiCall) {
    try {
      const response = await apiCall();
      return { status: response.status, data: response.data };
    } catch (error) {
      if (error.response) {
        return { status: error.response.status };
      }
      throw error;
    }
  }
  


export function isNotTokenNon() {
    return localStorage.getItem("token") !== "non"
}
