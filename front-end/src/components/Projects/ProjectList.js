import React from 'react'
import DeleteProject from "./DeleteProjects"
import swal from 'sweetalert';
import { FaTrash } from "react-icons/fa";
import { IoMdDoneAll } from "react-icons/io";
import EditProject from './EditProject';
import { Link } from "react-router-dom";
import { CircularProgressbar, buildStyles } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';
import { VscLoading } from "react-icons/vsc";


export default function ProjectList({ projects, id, name, description, status, tasksAmount, tasksLeft }) {


    const getStatusText = (statusBack) => {


        if (0 === tasksLeft && tasksAmount !== 0) {
            switch (statusBack) {
                case "IN_PROGRESS":
                    return <IoMdDoneAll style={{ color: "#1b6918" }} />;
                default:
                    return "status error";
            }
        }
        else {

            switch (statusBack) {
                case "IN_PROGRESS":
                    return <VscLoading style={{ color: "#ebe6a9" }} />;
                default:
                    return "status error";
            }
        }
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
            text: "Once deleted, you will not be able to recover this project!",
            icon: "warning",
            buttons: true,
            dangerMode: true
        })
            .then((willDelete) => {
                if (willDelete) {
                    DeleteProject({ id });
                    swal("Poof! Your project has been deleted!", {
                        icon: "success",
                    });
                    setTimeout(() => window.location.reload(), 1500);
                }
                else {
                    swal("Your project is safe!");
                    window.onpopstate = e => {
                        e.stopPropagation();
                    }
                }
            });
    }

    const tasksCompleteStyle = () => {
        if (tasksLeft === 0 && tasksAmount !== 0) {
            return "#ebecf1";
        }
        else {
            return "#f7f7f7";
        }
    }

    const [backColor, setBackColor] = React.useState(tasksCompleteStyle());
    const styles = {
        borderRadius: "10px",
        backgroundColor: backColor,
    }

    return (
        <div className="card text-left h-100 flex-fill shadow-lg"
            style={styles}
            onMouseEnter={() => setBackColor("#faf3f3")}
            onMouseLeave={() => setBackColor(tasksCompleteStyle())}>
            <Link to={`/projects/${id}/tasks`}
                style={{ textDecoration: "none", color: "black" }}>
                <div className="cardTop d-flex">
                    <div style={{ width: "60px", marginLeft: "10px", marginTop: "10px" }}>
                        <CircularProgressbar
                            value={tasksAmount - tasksLeft}
                            text={`${tasksAmount - tasksLeft}/${tasksAmount}`}
                            maxValue={tasksAmount}
                            styles={buildStyles({
                                pathColor: "#17a2b8",
                            })} />
                    </div>
                    <div>
                        <p className="card-text d-flex" style={{ marginLeft: "110px", fontSize: "50px", marginTop: "10px", marginRight: "1px" }}> {getStatusText(status)} </p>
                    </div>
                </div>
                <div className="card-body flex-fill">
                    <h5 className="card-title">{name}</h5>
                    <p className="card-text text-muted">{description}</p>
                </div>
            </Link>
            <div className="buttons mb-3 mr-3 d-flex" style={{ justifyContent: "flex-end" }}>
                <EditProject id={id} name={name} description={description} />
                <button onClick={() => getDeleteAlert()} className="btn btn-outline-danger my-2 my-sm-0 m-2" type="submit"><FaTrash />
                </button>
            </div>
        </div>
    )
}
