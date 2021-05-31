import React from "react";


const footerStyle = {
    backgroundColor: "#e1e5ea",
    textAlign: "center",
    padding: "1px",
    height: "auto",
    width: "100%"
};

export default function Footer() {
    return (
        <div style={footerStyle} className="card-text text-muted" >
            {'Copyright © '}
            <p style={{ fontSize: '1.5em' }} href="#">
                Emiliatorius'3
                   <p style={{ fontSize: '0.6em' }}> {new Date().getFullYear()}
                </p>
            </p>
        </div>
    )
}
