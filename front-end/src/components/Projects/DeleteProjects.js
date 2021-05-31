import React from 'react';
import axios from 'axios';

const DeleteProject = ({ id }) => {

    const user = JSON.parse(localStorage.getItem("token"));

    axios.delete(`http://localhost:8081/api/projects/${id}`, {
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

export default DeleteProject;