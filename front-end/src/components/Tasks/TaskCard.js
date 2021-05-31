import React from 'react'
import { FaTrash } from "react-icons/fa";
import DeleteTask from './DeleteTask';
import swal from 'sweetalert';
import EditTask from './EditTask'
import Dropdown from 'react-bootstrap/Dropdown'
import Card from 'react-bootstrap/Card'
import ListGroup from "react-bootstrap/ListGroup"

export default function TaskCard({ id, name, userStory, priority, status, creationDate, updateDate }) {

    // const getStatusText = (statusBack) => {
    //     switch (statusBack) {
    //         case "TO_DO":
    //             return "To Do";
    //         case "IN_PROGRESS":
    //             return "In Progress";
    //         case "DONE":
    //             return "Done";

    //         default:
    //             return "status error";
    //     }
    // }

    const getPriorityStyle = () => {
        let color;
        switch (priority) {
            case "LOW":
                color = "#55a366";
                break;
            case "MEDIUM":
                color = "#e69c35";
                break;
            case "HIGH":
                color = "red";
                break;
            default:
                color = "black";
        }
        return color;
    }

    const getDeleteAlert = () => {
        window.onpopstate = e => {
            e.preventDefault();
            window.history.forward();
            swal.close();
            console.log("you clicked back button");
        }
        swal({
            title: "Are you sure?",
            text: "Once deleted, you will not be able to recover this task!",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
            .then((willDelete) => {
                if (willDelete) {
                    DeleteTask({ id });
                    swal("Poof! Your task has been deleted!", {
                        icon: "success",
                    });
                    setTimeout(() => window.location.reload(), 1500);
                }
                else {
                    swal("Your task is safe!");
                    window.onpopstate = e => {
                        e.stopPropagation();
                    }
                }
            });
    }

    const [backColor, setBackColor] = React.useState("#f7f7f7");
    const styles = {
        borderRadius: "10px",
        backgroundColor: backColor
    }

    return (
        <div>
            <div className="card text-left shadow" 
            style={styles}
            onMouseEnter={() => setBackColor("#faf3f3")}
            onMouseLeave={() => setBackColor("#f7f7f7")}>
                <div className="card-body">
                    <div className="cardTop d-flex">
                        <h5 className="card-title" numberoflines={1} style={{ width: "290px" }}>{name}</h5>
                        <p className="card-text" style={{ color: getPriorityStyle(), width: "120px", marginRight: "1px", fontSize: "20px" }} >{priority}</p>
                    </div>
                    <p className="card-text text-muted">{userStory}</p>
                    <div className="buttons" style={{ display: "flex", float: "right" }}>
                        <EditTask id={id} name={name} userStory={userStory} priority={priority} />
                        <button onClick={() => getDeleteAlert()} className="btn btn-outline-danger btn-sm my-2 my-sm-0 m-2" type="submit"><FaTrash /></button>
                        <Dropdown>
                            <Dropdown.Toggle variant="outline-secondary btn-sm" id="dropdown-basic"></Dropdown.Toggle>
                            <Dropdown.Menu style={{ backgroundColor: "#faf3f3", borderRadius: "10px" }}>
                                <Card style={{ width: '19rem', backgroundColor: "#faf3f3", borderRadius: "10px", padding: "0", borderColor: "#faf3f3" }}>
                                    <ListGroup.Item style={{ backgroundColor: "#faf3f3", borderColor: "#faf3f3" }}>Creation Date: {creationDate}</ListGroup.Item>
                                    <ListGroup.Item style={{ backgroundColor: "#faf3f3", borderColor: "#faf3f3" }}>Update Date: {updateDate} </ListGroup.Item>
                                    <ListGroup.Item style={{ backgroundColor: "#faf3f3", borderColor: "#faf3f3" }}>Task ID: {id}</ListGroup.Item>
                                </Card>
                            </Dropdown.Menu>
                        </Dropdown>
                    </div>
                </div>
            </div>
        </div>
    )
}
