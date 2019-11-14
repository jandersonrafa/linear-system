import api from './api';

export const calculate= (body) => {
  return api.post('/gauss/calculate', body)
    .then(res => res.data)
}

export const getEmitter = (id) => {
  return new EventSource("http://localhost:8082/api/gauss/emitter/" + id);
}
