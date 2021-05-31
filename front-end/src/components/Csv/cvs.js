import React from 'react';
import axios from 'axios';


    

    function clickedOnDownloadButton() {
        axios.get({
          url: 'http://localhost:8081/export'
        }).then(function(signed) {
          window.location = signed.url;
        });
      
      
      
      

    return (
        <>
        </>
    )
}

export default clickedOnDownloadButton;