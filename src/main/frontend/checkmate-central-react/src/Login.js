import { useState } from "react";
import { SERVER_URL } from "./env.js";
import {Link, useNavigate} from "react-router-dom";

import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import {Container, FormGroup} from "react-bootstrap";


function LoginForm({setParentState}) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [validated, setValidated] = useState(false);
  const navigate = useNavigate();

  const setAuthenticated = () => {
    setParentState(true);
  }

  async function login(e) {
    e.preventDefault();

    const form = e.currentTarget;
    if(form.checkValidity() === true) {
      const loginUrl = SERVER_URL + '/api/auth/login';

      let requestBody = {
        email: email,
        password: password,
      }

      const requestheader = {
        'content-type': 'application/json',
      }

      try{
        const response = await fetch(loginUrl, {
          headers: requestheader,
          body: JSON.stringify(requestBody),
          method: "POST"
        });

        if(response.status === 200) {
          const data = await response.json();
          localStorage.setItem("accessToken", data.token);
          localStorage.setItem("tokenExpiry", data.expiry);
          localStorage.setItem("userName", data.username);
          setAuthenticated(true);
          navigate("/createTournament");

        } else {
          const error = await response.json();
          console.log(error.message);
        }

      } catch (error) {
        console.log(error.message);
      }
    }

    setValidated(true);
  }

  return (
    <>
      <div className="hero-title container">
        <div className="row justify-content-center">
        <h1>Checkmate Central</h1>
          <br/>
        <h2>Log in to your account</h2>
        </div>
      </div>

      <Container>
        <Form noValidate validated={validated} onSubmit={login}>
          <Row className="mb-3">
            <Form.Group controlId="validationCustom01">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                required
                type="email"
                placeholder="User Email"
                id="emailInput" aria-describedby="emailHelp"
                value={email} onChange={e => setEmail(e.target.value)}
              />
              <Form.Control.Feedback type="invalid">Email is empty</Form.Control.Feedback>
            </Form.Group>
          </Row>
          <Row className="mb-3">
            <Form.Group controlId="validationCustom02">
              <Form.Label htmlFor="password" className="form-label">Password</Form.Label>
              <Form.Control
                required
                type="password"
                placeholder="Password"
                id="password"
                value={password} onChange={e => setPassword(e.target.value)}
              />
                <Form.Control.Feedback type="invalid">Password is empty</Form.Control.Feedback>
            </Form.Group>
          </Row>
          <Row className="mb-3">
            <Button type="submit" id="login-button">Login</Button>
            <p className="mt-1">No account? Register for one <Link to="/register">here</Link></p>
          </Row>
        </Form>
      </Container>
    </>
  );
}

export default LoginForm;
