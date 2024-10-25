import { useState } from "react";
import { SERVER_URL } from "./env.js";
import { useNavigate } from "react-router-dom";


function RegisterForm() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();

  async function register(e) {
    e.preventDefault();


    const registerUrl = SERVER_URL + '/api/auth/register';

    let requestBody = {
      name: name,
      email: email,
      password: password,
      grantedAuthorityString: "ROLE_ADMIN"
    }
    
    const requestheader = {
      'content-type': 'application/json',
    }


    try{
      console.log(requestheader);
      const response = await fetch(registerUrl, {
        headers: requestheader,
        body: JSON.stringify(requestBody),
        method: "POST"
      });

      if(response.status === 201) {
        navigate("/login");
      }


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
        <form onSubmit={register}>
          <div className="mb-3">
            <label htmlFor="usernameInput" className="form-label">Username</label>
            <input type="text" className="form-control" id="usernameInput" 
            value = {name} onChange={e => setName(e.target.value)}>
            </input>
          </div>
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
          <div className="mb-3">
            <label htmlFor="confirmPassword" className="form-label">Confirm Password</label>
            <input type="password" className="form-control" id="confirmPassword"></input>
          </div>
          <button type="submit" className="btn btn-primary" id="register-button">Register</button>
        </form>
      </div>
    </div>
  );
}

export default RegisterForm;
