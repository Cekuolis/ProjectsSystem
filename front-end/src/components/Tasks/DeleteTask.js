import React from 'react';
import axios from 'axios';

const DeleteTask = ({ id }) => {

    const user = JSON.parse(localStorage.getItem("token"));

    axios.delete(`http://localhost:8081/api/tasks/${id}`, {
        headers: {
            "Authorization": "Bearer " + user.token
        },
    })
        .then(response => {
            console.log(response.data)
        })
        .catch(err => {
            console.log(err);
        })

    return (
        <>
        </>
    )
}

export default DeleteTask;