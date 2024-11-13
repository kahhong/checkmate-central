import {Container, Dropdown, DropdownButton, Nav, Navbar} from "react-bootstrap";
import {useAuth} from "../hooks/AuthContext";

export const MyNavbar = () => {
  const {logout} = useAuth();
  return (
    <Navbar bg="light" data-bs-theme="light">
      <Container>
        <Navbar.Brand href="/#">Checkmate Central</Navbar.Brand>
        <Nav className="me-auto">
          <Nav.Link href="/dashboard">Tournaments</Nav.Link>
          {/*<Nav className="justify-content-end">*/}
            <DropdownButton title={window.localStorage.getItem('userName')} variant="white" align="end">
              <Dropdown.Item href="/profile">Profile</Dropdown.Item>
              {/* <Dropdown.Item href="/admin">Admin</Dropdown.Item> */}
              <Dropdown.Item onClick={logout}>Log Out</Dropdown.Item>
            </DropdownButton>
          {/*</Nav>*/}
        </Nav>
      </Container>
    </Navbar>
  );
};