import { useEffect, useState } from "react";
import './index.css';
import './vendor/bootstrap/css/bootstrap.min.css'

import Form from 'react-bootstrap/Form';

function Profile() {
    let [data, setData] = useState([]);

    const url = "/api/profile/user/" + localStorage.getItem("userName");
    const requestHeader = {
        'content-type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.accessToken
    }

    useEffect(() => {
        fetch(url, {
          headers: requestHeader,
          method: 'GET'
        }).then(response => response.json())
            .then(json => setData(json));
      }, [])

    return (
        <>
            <div className="hero-title container">
                <h1>User Profile</h1>
                <Form.Group className="mb-3">
                    <Form.Label>Username: {data.name}</Form.Label>
                    <Form.Control placeholder="Disabled input" disabled />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Email: {data.email}</Form.Label>
                    <Form.Control placeholder="Disabled input" disabled />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>ELO Rating: {data.rating}</Form.Label>
                    <Form.Control placeholder="Disabled input" disabled />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Time Last Played: {new Date(Date.parse(data.timeLastPlayed)).toString()}</Form.Label>
                    <Form.Control placeholder="Disabled input" disabled />
                </Form.Group>
            </div>
        </>
    )
}


export default Profile;
