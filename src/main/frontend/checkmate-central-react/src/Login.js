import { useState } from "react";
import { SERVER_URL } from "./env.js";
import { useNavigate } from "react-router-dom";


function LoginForm() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();


  async function login(e) {
    e.preventDefault();

    const loginUrl = SERVER_URL + '/api/auth/login';

    let requestBody = {
      email: email,
      password: password,
    }
    
    const requestheader = {
      'content-type': 'application/json',
    }


    try{
       fetch(loginUrl, {
        headers: requestheader,
        body: JSON.stringify(requestBody),
        method: "POST"
      })
       .then((response) => response.json())
       .then((data) => {
          localStorage.setItem("accessToken", data.token);
          localStorage.setItem("tokenExpiry", data.expiry);
          localStorage.setItem("userName", data.username);
      });
        
      navigate("/createTournament");

    } catch (error) {
        console.error(error.message);
    }
  }

  return (
    <div>
      <div className="hero-title container">
        <div className="row justify-content-center">
          <h1>Checkmate Central</h1>
        </div>
      </div>


      <div className="container register-dialog">
        <form onSubmit={login}>
          <div className="mb-3">
            <label htmlFor="emailInput" className="form-label">Email address</label>
            <input type="email" className="form-control" id="emailInput" aria-describedby="emailHelp"
              value={email} onChange={e => setEmail(e.target.value)}></input>
          </div>
          <div className="mb-3">
            <label htmlFor="password" className="form-label">Password</label>
            <input type="password" className="form-control" id="password"
              value={password} onChange={e => setPassword(e.target.value)}></input>
          </div>
          <button type="submit" className="btn btn-primary" id="register-button">Login</button>
        </form>
      </div>
    </div>
  );
}

export default LoginForm;
