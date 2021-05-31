import React, { useState } from 'react'
import axios from "axios";
import swal from 'sweetalert';
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import "bootstrap/dist/css/bootstrap.min.css";
import { RiEdit2Line } from "react-icons/ri";


export default function EditProject(id) {

  const [show, setShow] = useState(false);
  const handleClose = () => {
    setShow(false);
    setName(id.name);

  }
  const handleShow = () => {
    setShow(true);
    console.log(id);
    console.log(maxSymbolsName);
  }

  const API_URL = 'http://localhost:8081';
  const [name, setName] = useState(id.name);
  const [description, setDesc] = useState(id.description);
  const user = JSON.parse(localStorage.getItem("token"));
  const maxSymbolsName = 50;
  const maxSymbolsDesc = 250;

  const submitProject = (e) => {
    e.preventDefault();
    console.log(id);
    return axios.put(API_URL + "/api/projects/" + id.id, {
      description,
      name
    },
      {
        headers: {
          'Authorization': 'Bearer ' + user.token
        }
      }
    )
      .then((response) => {

        swal("Bang! Your project has been updated!", {
          icon: "success",
        });
        setTimeout(() => window.location.reload(), 1500);
        handleClose();
        return response;
      },
        (error) => {
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

  return (
    <div>

      <Button variant="outline-info" onClick={handleShow} className="my-2 my-sm-0 " type="submit">
        <RiEdit2Line />
      </Button>
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton style={{ backgroundColor: "#faf3f3" }}>
          <Modal.Title>Edit Project</Modal.Title>
        </Modal.Header>
        <Modal.Body style={{ backgroundColor: "#faf3f3" }}>

          <form onSubmit={submitProject}>
            <div className="form-group">
              <label htmlFor="exampleInputEmail1">Project name</label>
              <input type="text" onChange={(e) => setName(e.target.value)} className="form-control m-2" placeholder="Project name" defaultValue={id.name} maxlength={maxSymbolsName} />
              <text className="text-muted float-right"> {name.length} / {maxSymbolsName}</text>
            </div>
            <div className="form-group">
              <label htmlFor="exampleInputPassword1">About project</label>
              <input type="text" onChange={(e) => setDesc(e.target.value)} className="form-control m-2" placeholder="Project is..." defaultValue={id.description} maxLength={maxSymbolsDesc} />
              <text className="text-muted float-right"> {description.length} / {maxSymbolsDesc}</text>
            </div>
          </form>

        </Modal.Body>
        <Modal.Footer style={{ backgroundColor: "#faf3f3" }}>
          <Button variant="outline-secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="info" onClick={submitProject}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  )
}
