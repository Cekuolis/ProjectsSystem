import React from "react";
import { useState } from "react";
import axios from "axios";
import { useHistory } from "react-router-dom";
import swal from 'sweetalert';
import Footer from './../home/Footer'

export default function Login() {
  const [mail, setMail] = useState("")
  const [password, setPassword] = useState("")
  const API_URL = 'http://localhost:8081/';
  const history = useHistory();
  const login = (e) => {
    e.preventDefault();
    // const user = { mail, password }
    return axios.post(API_URL + "authenticate", {
      mail,
      password
    })
      .then((response) => {
        // returns json from back with username and jwt token returns name as a string
        localStorage.setItem('token', JSON.stringify(response.data));
        console.log(localStorage.getItem('token'))
        history.push('/projects')
        // const name = Object.keys(response.data)
        return response.mail;
      },
        (error) => {
          console.log("opps something went wrong :)");
          swal({
            text: "Bad email or password!",
            icon: "error",
            button: "Try again",
          });
        });
  }

  const footerStyle = {
    textAlign: "center",
    padding: "1px",
    height: "auto",
    width: "100%",
    position: "fixed",
    bottom: "0",
  };

  return (
    <div>
      <div style={{
        backgroundImage: `url("https://www.ntaskmanager.com/wp-content/uploads/2019/07/just-sit.png")`,
        backgroundRepeat: "no-repeat",
        backgroundPosition: "center",
        backgroundSize: "cover",
        position: "relative",
      }}>
        <div className="container">
          <div className="row">
            <div className="col-12 col-sm-12 col-md-4 col-lg-4 col-xl-3">
              <div className="card my-5 py-5 px-3" style={{ backgroundColor: "#e1e5ea", borderRadius: "15px", opacity: "90%" }}>
                <form className="text-center" onSubmit={login} >

                  <h4>Sign In </h4>
                  <div className="form-group p-2">
                    <label>Email address</label>
                    <input type="email" onChange={(e) => setMail(e.target.value)} className="form-control m-2" placeholder="Enter email" />
                  </div>

                  <div className="form-group p-2">
                    <label>Password</label>
                    <input type="password" onChange={(e) => setPassword(e.target.value)} className="form-control m-2" placeholder="Enter password" />
                  </div>
                  <button type="submit" className="btn btn-info btn-block">Submit</button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div style={footerStyle} className="card-text text-muted" >
        {'Copyright © '}
        <p style={{ fontSize: '1.5em' }} href="#">
          Emiliatorius'3
                   <p style={{ fontSize: '0.6em' }}> {new Date().getFullYear()}
          </p>
        </p>
      </div>
    </div>
  );
}
