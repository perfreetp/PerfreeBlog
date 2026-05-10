export function noteAddApi(data) {
    return axios.post('/api/auth/note/createNote', data);
}

export function notePageApi(data) {
    return axios.post('/api/auth/note/page', data);
}

export function noteDelApi(id) {
    return axios.delete('/api/auth/note/del?id=' + id);
}

export function noteGetApi(id) {
    return axios.get('/api/auth/note/get?id=' + id);
}

export function updateNoteApi(data) {
    return axios.put('/api/auth/note/updateNote', data);
}
