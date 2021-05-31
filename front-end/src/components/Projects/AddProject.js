import React, { useState } from 'react'
import { IoAddOutline } from "react-icons/io5";
import axios from "axios";
import swal from 'sweetalert';
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import "bootstrap/dist/css/bootstrap.min.css";

export default function AddProject() {

    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const maxSymbolsName = 50;
    const maxSymbolsDesc = 250;
    const API_URL = 'http://localhost:8081';
    const [name, setName] = useState("");
    const [description, setDesc] = useState("");
    const user = JSON.parse(localStorage.getItem("token"));

    const submitProject = (e) => {
        e.preventDefault();
        return axios.post(API_URL + "/api/projects", {
            description,
            name
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
                // console.log(error.response.status);

                switch (error.response.status) {

                    case 400:
                        swal({
                            text: "No project name or description!",
                            icon: "warning",
                            button: "Try again",
                        });
                        break;

                    case 500:
                        swal({
                            text: "The Project name exists!",
                            icon: "warning",
                            button: "Try again",
                        });
                        break;

                    default:
                        break;
                }
            }
        );
    }

    const [backColor, setBackColor] = React.useState("#f7f7f7");
    const styles = {
        borderRadius: "10px",
        cursor: "pointer",
        backgroundColor: backColor
    }

    return (
        <div>
            <div className="card text-center card-body flex-fill shadow-lg"
                onClick={handleShow}
                style={styles}
                onMouseEnter={() => setBackColor("#faf3f3")}
                onMouseLeave={() => setBackColor("#f7f7f7")}>
                <div className="card-body">
                    <IoAddOutline style={{ fontSize: 100 }} />
                    <p>Create new project</p>
                </div>
            </div>


            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton style={{ backgroundColor: "#faf3f3" }}>
                    <Modal.Title>Create Project</Modal.Title>
                </Modal.Header>
                <Modal.Body style={{ backgroundColor: "#faf3f3" }}>
                    <form onSubmit={submitProject}>
                        <div className="form-group">
                            <label htmlFor="exampleInputEmail1">Project name</label>
                            <input type="text" onChange={(e) => setName(e.target.value)} className="form-control m-2" placeholder="Project name" maxlength={maxSymbolsName} />
                            <text className="text-muted float-right"> {name.length} / {maxSymbolsName}</text>
                        </div>
                        <div className="form-group">
                            <label htmlFor="exampleInputPassword1">About project</label>
                            <input type="text" onChange={(e) => setDesc(e.target.value)} className="form-control m-2" placeholder="Project is..." maxLength={maxSymbolsDesc} />
                            <text className="text-muted float-right"> {description.length} / {maxSymbolsDesc}</text>
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
