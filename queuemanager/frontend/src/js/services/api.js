import axios from "axios";

const api = axios.create({
    baseURL: "http://3.87.169.203:8081/api"
});

export default api;