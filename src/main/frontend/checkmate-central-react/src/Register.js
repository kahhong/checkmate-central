import { useState } from "react";
import { SERVER_URL } from "./env.js";
import { useNavigate } from "react-router-dom";

import Modal from "react-bootstrap/Modal"
import {Button, ModalHeader, ModalTitle} from "react-bootstrap";


// const MyModal = (title, message, handleClose) => {
//
//   const handleClose = () => setShowModal(false);
//
//   return (
//       <>
//         <Modal show={show} onHide={handleClose}>
//           <ModalHeader closeButton>
//             <Modal.Title>{title}</Modal.Title>
//           </ModalHeader>
//           <Modal.Body>{message}</Modal.Body>
//           <Modal.Footer>
//             <Button variant="primary" onClick={handleClose}>
//               Ok
//             </Button>
//           </Modal.Footer>
//         </Modal>
//       </>
//   );
// }

function RegisterForm() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  // const [showModal, setShowModal] = useState(false);

  // const handleClose = () => setShowModal(false);


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
    
    const requestHeader = {
      'content-type': 'application/json',
    }

    try{
      const response = await fetch(registerUrl, {
        headers: requestHeader,
        body: JSON.stringify(requestBody),
        method: "POST"
      });

      const json = await response.json();

      if(response.status === 201) {
        navigate("/login");

      } else {
        // setShowModal(true)
      }
    } catch (error) {
      
      console.error(error.message);

    }
  }

  return (
    <div>
      {/*<Modal show={show} onHide={handleClose}>*/}
      {/*  <ModalHeader closeButton>*/}
      {/*    <Modal.Title>{title}</Modal.Title>*/}
      {/*  </ModalHeader>*/}
      {/*  <Modal.Body>{message}</Modal.Body>*/}
      {/*  <Modal.Footer>*/}
      {/*    <Button variant="primary" onClick={handleClose}>*/}
      {/*      Ok*/}
      {/*    </Button>*/}
      {/*  </Modal.Footer>*/}
      {/*</Modal>*/}

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
