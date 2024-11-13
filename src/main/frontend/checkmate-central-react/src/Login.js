import { Link, useNavigate } from "react-router-dom";
import { useEffect } from "react";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import { Container } from "react-bootstrap";
import { Formik } from "formik";
import * as Yup from "yup";

import { useAuth } from './hooks/AuthContext';

function Login() {
  const navigate = useNavigate();
  const { setLogin, isLoggedIn } = useAuth();
  
  useEffect(() => {
    if (isLoggedIn) {
      console.log("Redirect to dashboard");
      navigate('/dashboard');
    }
  }, [isLoggedIn, navigate]);

  async function login(values) {
    const loginUrl = '/api/auth/login';

    let requestBody = {
      email: values.email,
      password: values.password,
    }

    const requestHeader = {
      'content-type': 'application/json',
    }

    try {
      const response = await fetch(loginUrl, {
        headers: requestHeader,
        body: JSON.stringify(requestBody),
        method: "POST"
      });

      if (response.status === 200) {
        const data = await response.json();
        localStorage.setItem("id", data.id);
        localStorage.setItem("accessToken", data.token);
        localStorage.setItem("tokenExpiry", data.expiry);
        localStorage.setItem("userName", data.username);
        setLogin({
          username: data.username,
          token: data.token
        });
        navigate('/dashboard');
      } else {
        return response;
      }

    } catch (error) {
      console.log(error.message);
    }
  }

  const LoginSchema = Yup.object().shape({
    email: Yup.string().required('Email is empty').email('Invalid email'),
    password: Yup.string().required('Password is empty')
      .min(8, 'Password should be at least 8 characters')
      .max(30, 'Password cannot exceed 30 characters'),
  });

  return (
    <>
      <Container className="hero-title">
        <Row className="justify-content-center">
          <h1>Checkmate Central</h1>
          <br />
          <h2>Log in to your account</h2>
        </Row>
      </Container>

      <Formik
        validationSchema={LoginSchema}
        onSubmit={(values, { setSubmitting, setFieldError }) => {
          login(values)
            .then(response => {
              if (response !== undefined && response.status !== 200) {
                response.json().then(({ message }) => {
                  setFieldError('email', message);
                  setFieldError('password', message);
                  setSubmitting(false);
                })
              }
            }
            )
        }}
        initialValues={{
          email: '',
          password: ''
        }}
      >
        {({ setFieldTouched, handleSubmit, handleChange, values, touched, errors }) => (
          <Container>
            <Form noValidate onSubmit={handleSubmit}>
              <Row className="mb-3">
                <Form.Group controlId="validationCustom01">
                  <Form.Label>Email address</Form.Label>
                  <Form.Control
                    required
                    type="email"
                    placeholder="johndoe@example.com"
                    name="email"
                    value={values.email}
                    onChange={e => {
                      setFieldTouched('email');
                      handleChange(e);
                    }}
                    isInvalid={touched.email && errors.email}
                  />
                  <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                </Form.Group>
              </Row>
              <Row className="mb-3">
                <Form.Group controlId="password">
                  <Form.Label className="form-label">Password</Form.Label>
                  <Form.Control
                    required
                    type="password"
                    placeholder="Password"
                    name="password"
                    value={values.password}
                    onChange={e => {
                      setFieldTouched('password');
                      handleChange(e);
                    }}
                    isInvalid={touched.password && errors.password}
                  />
                  <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                </Form.Group>
              </Row>
              <Row className="mb-3">
                <Form.Group>
                  <Button type="submit" id="login-button">Login</Button>
                </Form.Group>
                <p className="mt-1">No account? Register for one <Link to="/register">here</Link></p>
              </Row>
            </Form>
          </Container>
        )}
      </Formik>
    </>
  );
}

export default Login;
