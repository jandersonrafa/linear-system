import axios from "axios";

const api = axios.create({
    // baseURL: "http://localhost:8082/api"
    baseURL: "http://39f0dd9c.ngrok.io/api"
});

export default api;