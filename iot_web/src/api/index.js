import axios from "axios";


axios.interceptors.request.use(config => {
    config.baseURL = '/api';
    config.headers["Accept"] = "application/json";
    config.headers["Content-Type"] = "application/json";
    return config;
}, error => {
    return Promise.reject(error);
})


export const userLogin = param => {
    return axios.post(`/iot/iot_user/login`, JSON.stringify(param)).then(
        res => res.data
    );
}

export const deviceHeartBeet = param => {
    return axios.post(`/iot/iot_device/page`, JSON.stringify(param)).then(
        res => res.data
    );
}

export const deviceWarn = param => {
    return axios.post(`/iot/iot_warn/page`, JSON.stringify(param)).then(
        res => res.data
    );
}


