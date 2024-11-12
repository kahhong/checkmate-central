import {BASE_URL} from "./env.js";
import { useNavigate } from "react-router-dom";

import {Button, Container} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import {Formik} from "formik";
import * as Yup from "yup";

function RegisterForm() {
  const navigate = useNavigate();

  async function register({usernameInput, emailInput, password}) {
    const registerUrl = BASE_URL + '/api/auth/register/player';
    console.log(registerUrl)

    let requestBody = {
      name: usernameInput,
      email: emailInput,
      password: password
    }

    const requestHeader = {
      'content-type': 'application/json',
    }

    try {
      const response = await fetch(registerUrl, {
        headers: requestHeader,
        body: JSON.stringify(requestBody),
        method: "POST"
      });

      if (response.status === 201) {
        navigate("/login");
      }

    } catch (error) {
      console.error(error.message);
    }
  }


  const RegisterSchema = Yup.object().shape({
    usernameInput: Yup.string().required('Username is empty'),
    emailInput: Yup.string().email('Invalid email address').required('Email address is empty'),
    password: Yup.string().required('Password is empty')
      .min(8, 'Password should be at least 8 characters')
      .max(30, 'Password cannot exceed 30 characters'),
    confirmPassword: Yup.string().required('Password is empty')
      .equals([Yup.ref("password")], "Passwords do not match")
  });

  return (
    <>
      <Container className="hero-title">
        <Row className="justify-content-center">
          <h1>Checkmate Central</h1>
          <h2>Register a new account </h2>
        </Row>
      </Container>

      <Formik
        validationSchema={RegisterSchema}
        onSubmit={register}
        initialValues={{
          usernameInput: '',
          emailInput: '',
          password: '',
          confirmPassword: ''
        }}
      >
        {({ setFieldTouched, handleSubmit, handleChange, values, touched, errors }) => (
          <Container className="register-dialog">
            <Form noValidate onSubmit={handleSubmit}>
              <Row className="mb-3">
                <Form.Group controlId="username">
                  <Form.Label>Username</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="John Doe"
                    name="usernameInput"
                    onChange={ e => {
                      setFieldTouched('usernameInput');
                      handleChange(e);
                    }}
                    value = { values.usernameInput }
                    isInvalid={ touched.usernameInput && errors.usernameInput }
                  />
                  <Form.Control.Feedback type="invalid">
                    { errors.usernameInput }
                  </Form.Control.Feedback>
                </Form.Group>
              </Row>

              <Row className="mb-3">
                <Form.Group controlId="email">
                  <Form.Label>Email address</Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="johndoe@example.com"
                    name="emailInput"
                    value = { values.emailInput }
                    onChange={ e => {
                      setFieldTouched('emailInput');
                      handleChange(e);
                    }}
                    isInvalid={ touched.emailInput && errors.emailInput }
                    />
                  <Form.Control.Feedback type="invalid">{ errors.emailInput }</Form.Control.Feedback>
                </Form.Group>
              </Row>

              <Row className="mb-3">
                <Form.Group controlId="password">
                  <Form.Label>Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Password"
                    name="password"
                    value = { values.password }
                    onChange={ e => {
                      setFieldTouched('password');
                      handleChange(e);
                    }}
                    isValid={ touched.password && !errors.password  }
                    isInvalid={ touched.password && errors.password }
                  />
                  <Form.Control.Feedback type="invalid">{ errors.password }</Form.Control.Feedback>
                </Form.Group>
              </Row>

              <Row className="mb-3">
                <Form.Group controlId="confirmPassword">
                  <Form.Label>Confirm Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Confirm password"
                    name="confirmPassword"
                    value = { values.confirmPassword }
                    onChange={ e => {
                      setFieldTouched('confirmPassword');
                      handleChange(e);
                    }}
                    isValid={ touched.confirmPassword && !errors.confirmPassword }
                    isInvalid={ touched.confirmPassword && errors.confirmPassword }
                  />
                  <Form.Control.Feedback type="invalid">{ errors.confirmPassword }</Form.Control.Feedback>
                </Form.Group>
              </Row>

              <Row className="mb-3">
                <Button type="submit" id="register-button">Register</Button>
              </Row>
            </Form>
          </Container>
        )}
      </Formik>
    </>
  );
}

export default RegisterForm;
