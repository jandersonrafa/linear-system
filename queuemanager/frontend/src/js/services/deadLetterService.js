import api from './api';

export const resubmit= (id, body) => {
  return api.put('/dead-letter/resubmit/' + id, body)
    .then(res => res.data)
}

export const remove= (id, body) => {
  return api.put('/dead-letter/delete/' + id, body)
    .then(res => res.data)
}

export const findAll= (params) => {
  return api.get('/dead-letter/')
    .then(res => res.data)
}
