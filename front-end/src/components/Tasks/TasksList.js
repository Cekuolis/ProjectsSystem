import React, { useState, useEffect } from 'react'
import axios from 'axios'
import NavBar from "../home/Navbar"
import TaskCard from './TaskCard';
import AddTask from './AddTask';
import { useHistory } from "react-router-dom";
import ExportTask from '../Tasks/ExportTasks';
import Footer from '../home/Footer';
import { DragDropContext, Draggable, Droppable } from "react-beautiful-dnd";

const Tasks = ({ match }) => {

  const [tasks, setTasks] = useState([]);
  const [name, setName] = useState("");
  const user = JSON.parse(localStorage.getItem("token"));
  const [projectName, setProjectName] = useState("");

  useEffect(() => {
    fetchTask();
  }, [name])

  const fetchTask = async () => {
    await axios(
      `http://localhost:8081/api/projects/${match.params.id}/tasks?name=` + name, {
      headers: {
        "Authorization": "Bearer " + user.token
      },
    })
      .then(response => {
        setTasks(response.data.tasks);
        // console.log(response.data.project);
        setProjectName(response.data.project);

      })
      .catch(err => {
        console.log(err);
      })
  }
  // const projectName = () => {
  //   return response.date.project;
  // }

  const columnsFromBackend = {
    "TO_DO": {
      name: "To Do",
      status: "TO_DO",
    },
    "In_Progress": {
      name: "In Progress",
      status: "IN_PROGRESS",
    },
    "Done": {
      name: "Done",
      status: "DONE",
    }
  };

  const [columns, setColumns] = useState(columnsFromBackend);


  const onDragEnd = (result, columns, setColumns) => {
    if (!result.destination) return;
    const { source, destination } = result;

    if (source.droppableId !== destination.droppableId) {
      console.log(result.draggableId)
      const status = result.destination.droppableId.toUpperCase()

      axios.put(
        `http://localhost:8081/api/tasks/${result.draggableId}/status?status=${status}`, {
      },
        {
          headers: {
            "Authorization": "Bearer " + user.token
          },
        })
        .then(response => {
          fetchTask()
          console.log(response)
        })
        .catch(err => {
          console.log(err);
        })
    }
  };

  const history = useHistory();
  const backToProjects = () => {
    history.push("/projects");
  }

  return (
    <div style={{ backgroundColor: "#e1e5ea" }}>
      <NavBar />
      <div className="container-fluid">
        <div className="row">
          <div className="col-5 text-center">
            <div className="buttons mr-3 d-flex">
              <button type="submit" onClick={backToProjects} className="btn btn-light btn ml-3">Go back to projects</button>
              <ExportTask project={match.params.id} />
              <AddTask id={match.params.id} />
            </div>
          </div>
          <div className="col-2">
            <p className="text-center" style={{ fontSize: "20px" }}>{projectName}</p>
          </div>
          <div className="col-5">
            <form>
              <div className="form-group mx-2" style={{ display: "flex", float: "right" }}>
                <label htmlFor="name" style={{ margin: "7px" }}> Search</label>
                <input onChange={e => setName(e.target.value)} type="search" className="form-control" id="name" placeholder="Task name or id" />
              </div>
            </form>
          </div>
        </div>
      </div>

      <div className="container-fluid">
        <div className="row py-1 justify-content-center">
        </div>
        <div className="col-12">
          <div style={{ display: "flex", justifyContent: "center", height: "100%" }}>

            <DragDropContext
              onDragEnd={result => onDragEnd(result, columns, setColumns)}
            >
              {Object.entries(columns).map(([columnId, column], index) => {
                return (
                  <div
                    style={{ display: "flex", flexDirection: "column", alignItems: "center" }}
                    key={columnId}
                  >
                    <h4>{column.name}</h4>
                    <div style={{ margin: 1 }}>
                      <Droppable droppableId={columnId} key={columnId}>
                        {(provided, snapshot) => {
                          return (
                            <div className="shadow-lg"
                              {...provided.droppableProps}
                              ref={provided.innerRef}
                              style={{ padding: 4, width: 400, minHeight: 1000, margin: 4, backgroundColor: "#f0f4fa", borderRadius: "10px" }}
                            >
                              {tasks.filter(task => task.status === column.status).map((task, index) => {
                                return (
                                  <Draggable
                                    key={task.id}
                                    draggableId={'' + task.id}
                                    index={index}
                                  >
                                    {(provided, snapshot) => {
                                      return (
                                        <div
                                          ref={provided.innerRef}
                                          {...provided.draggableProps}
                                          {...provided.dragHandleProps}
                                          style={{
                                            userSelect: "none", padding: 10, margin: "0 0 1px 0",
                                            minHeight: "50px",
                                            ...provided.draggableProps.style
                                          }}
                                        >
                                          <TaskCard key={task.id} tasks={tasks} id={task.id} name={task.name} userStory={task.userStory} priority={task.priority} status={task.status} creationDate={task.creationDate} updateDate={task.updateDate} />
                                        </div>
                                      );
                                    }}
                                  </Draggable>
                                );
                              })}
                              {provided.placeholder}
                            </div>
                          );
                        }}
                      </Droppable>
                    </div>
                  </div>
                );
              })}
            </DragDropContext>
          </div>
        </div>
      </div>
      <Footer />
    </div>

  )
}

export default Tasks;