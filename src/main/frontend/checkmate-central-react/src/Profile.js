import { useEffect, useState } from "react";
import './index.css';
import './vendor/bootstrap/css/bootstrap.min.css'

import Form from 'react-bootstrap/Form';

function Profile() {
  const [data, setData] = useState({});
  
  function setProfileData(json) {
    const profile = json.player

    if (!profile) {
        console.log("Profile not found");
    } else {
        setData(profile);
    }
  }

  const requestHeader = {
      'content-type': 'application/json',
      'Authorization': 'Bearer ' + localStorage.accessToken
  }
  
  function setAvailability(isActive) {
    let newData = { ...data };

    fetch("/api/player/" + localStorage.id + "/availability", {
      headers: requestHeader,
      method: 'PUT',
      body: JSON.stringify({ availability: isActive })
    })
    .then(response => response.json())
    .then(json => newData.availability = json.availability);

    window.location.reload();
  }

  const url = "/api/player/" + localStorage.id + "/profile";

  useEffect(() => {
    fetch(url, {
        headers: requestHeader,
        method: 'GET'
    }).then(response => response.json())
      .then(setProfileData);
  }, [])

  return (
    <>
      <div className="hero-title container">
        <h1>User Profile</h1>
        <Form.Group className="mb-3">
          <Form.Label>Username: {data.name}</Form.Label>
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Email: {data.email}</Form.Label>
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>ELO Rating: {data.rating}</Form.Label>
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Time Last Played: {new Date(Date.parse(data.timeLastPlayed)).toString()}</Form.Label>
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Check label="Available for Tournament"
            type="switch"
            checked={data.availability}
            onChange={e => setAvailability(e.currentTarget.checked)}
          />
        </Form.Group>
      </div>
    </>
  )
}


export default Profile;
