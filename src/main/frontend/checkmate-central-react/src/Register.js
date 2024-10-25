import { useState } from "react";



function RegisterForm() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');


  function submitForm(e) {
    e.preventDefault();

    let xhttp = new XMLHttpRequest();
      let requestBody = {
        name: name,
        email: email,
        password: password,
        grantedAuthorityString: "ROLE_USER"
      }
  
      xhttp.onreadystatechange = function() {
          if (this.readyState === XMLHttpRequest.DONE && this.status === 201)  {
              window.location.href = "/dashboard.html";
              
          } else if (this.readyState === XMLHttpRequest.DONE) {
              console.error(this.responseText);
          }
      };
  
      xhttp.open("POST", "http://localhost:8080/auth/register", true);
      xhttp.setRequestHeader('Content-type', 'application/json');
      xhttp.send(JSON.stringify(requestBody));
  }


  return (
    <div>
      <div className="hero-title container">
        <div className="row justify-content-center">
          <h1>Checkmate Central</h1>
        </div>
      </div>


      <div className="container register-dialog">
        <form onSubmit={submitForm}>
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
