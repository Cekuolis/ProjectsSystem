import React from 'react'

export default function UserName() {

    const user = JSON.parse(localStorage.getItem('token'));
    const a = user.mail;

    return (
        <div>
              <p className="navbar-brand col-sm-3 col-md-2 mr-0" href="#">{a}</p>
        </div>
    )
}
