import Button from "react-bootstrap/Button";
import "bootstrap/dist/css/bootstrap.min.css";


export default function ExportTask( project ) {

  const API_URL = 'http://localhost:8081';
  const user = JSON.parse(localStorage.getItem("token"));


  const downloadData = () => {
    fetch(API_URL + `/api/tasks/export/${project.project}`, {
      headers: {
        'Authorization': 'Bearer ' + user.token
      }
    })
      .then(response => {
        response.blob().then(blob => {
          let url = window.URL.createObjectURL(blob);
          let a = document.createElement('a');
          a.href = url;
          a.download = 'Tasks.csv';
          a.click();
        });
      });
  }

  return (
    <div>

      <Button variant="light" onClick={downloadData} className="btn ml-3 mr-3">
        Export tasks
      </Button>

    </div>
  )
}