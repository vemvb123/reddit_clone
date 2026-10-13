import axios from 'axios';


export function createAxiosInstance() {
  const token = localStorage.getItem("token");

  return axios.create({
    baseURL: "http://localhost:8080",
    headers: {
      // "Content-Type": "application/json",
      ...(token && { Authorization: `Bearer ${token}` }),
    },
  });
}

  export async function request(apiCall) {
    try {
      const response = await apiCall();
      console.log("ingen feil")
      return { status: response.status, data: response.data };
    } catch (error) {
      console.log("feil")
      if (error.response) {
        return []
        // return { status: error.response.status };
      }
      throw error;
    }
  }
  


export function hasToken() {
    return localStorage.getItem("token") !== null
}

export function defaultUserImage(seed = "user") {
    return `https://api.dicebear.com/10.x/initials/svg?seed=${encodeURIComponent(seed)}&backgroundColor=e0e7ff,c7d2fe,ddd6fe`;
}

export function defaultCommunityImage(seed = "community") {
    return `https://api.dicebear.com/10.x/shapes/svg?seed=${encodeURIComponent(seed)}&backgroundColor=f1f5f9,e2e8f0,dbeafe`;
}


export async function handleRequest(requestFn) {
    try {
        const response = await request(requestFn);
        const data = response.data;

        if (response.status !== 200) {
            return {
                data: null,
                error: {
                    message: data?.message ?? "Something went wrong",
                    code: data?.code ?? null,
                },
                status: response.status,
            };
        }

        return {
            data,
            error: null,
            status: response.status,
        };

    } catch (error) {
        return {
            data: null,
            error: {
                message: error instanceof Error
                    ? error.message
                    : "An unexpected error occurred",
                code: null,
            },
            status: 0,
        };
    }
}
