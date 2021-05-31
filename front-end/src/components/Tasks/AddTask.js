import React, { useState } from 'react'
import axios from "axios";
import swal from 'sweetalert';
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import "bootstrap/dist/css/bootstrap.min.css";


export default function AddTask(id) {

    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const maxSymbolsName = 50;
    const maxSymbolsStory = 250;
    const API_URL = 'http://localhost:8081';
    const [name, setName] = useState("");
    const [userStory, setUserStory] = useState("");
    const [priority, setPriority] = useState("MEDIUM");
    const user = JSON.parse(localStorage.getItem("token"));


    const submitProject = (e) => {
        e.preventDefault();
        return axios.post(API_URL + "/api/projects/" + id.id + "/tasks", {
            name,
            priority,
            userStory
        },
            {
                headers: {
                    'Authorization': 'Bearer ' + user.token
                }
            }
        ).then((response) => {
            window.location.reload(true);
            return response;
        },
            (error) => {
                swal({
                    text: "No task name!",
                    icon: "warning",
                    button: "Try again",
                });
            }
        );
    }


    return (
        <div>
            {/* <div className="card text-center" onClick={handleShow}
                style={styles}
                onMouseEnter={() => setBackColor("#faf3f3")}
                onMouseLeave={() => setBackColor("")}>
                <div className="card-body d-flex pt-2">
                    <IoAddOutline style={{ fontSize: 20, marginRight: "10px" }} />
                    <p>Create new task</p>
                </div>
            </div> */}
            <Button variant="light" className="btn mr-3" onClick={handleShow}>
                + Create new task
            </Button>

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton style={{ backgroundColor: "#faf3f3" }}>
                    <Modal.Title>Create Task</Modal.Title>
                </Modal.Header>
                <Modal.Body style={{ backgroundColor: "#faf3f3" }}>
                    <form onSubmit={submitProject}>
                        <div className="form-group">
                            <label htmlFor="exampleInputEmail1">Task name</label>
                            <input type="text" onChange={(e) => setName(e.target.value)} className="form-control m-2" placeholder="Task name" maxlength={maxSymbolsName} required />
                            <text className="text-muted float-right"> {name.length} / {maxSymbolsName}</text>
                        </div>
                        <div className="form-group">
                            <label htmlFor="exampleInputPassword1">User story</label>
                            <input type="text" onChange={(e) => setUserStory(e.target.value)} className="form-control m-2" placeholder="User story" maxLength={maxSymbolsStory} />
                            <text className="text-muted float-right"> {userStory.length} / {maxSymbolsStory}</text>
                        </div>
                        <div className="form-group">
                            <label htmlFor="priority">Priority</label>
                            <select onChange={(e) => setPriority(e.target.value)} className="custom-select" >
                                <option value="LOW">Low</option>
                                <option selected value="MEDIUM">Medium</option>
                                <option value="HIGH">High</option>
                            </select>
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer style={{ backgroundColor: "#faf3f3" }}>
                    <Button variant="outline-secondary" onClick={handleClose}>
                        Close
          </Button>
                    <Button variant="info" onClick={submitProject}>
                        Create
          </Button>
                </Modal.Footer>
            </Modal>
        </div>
    )
}