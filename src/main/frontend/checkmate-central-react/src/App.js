import logo from './logo.svg';
import './App.css';
import {useState} from "react";
import Login from "./Login";
import Dashboard from "./Dashboard";

function App() {
  const [isAuthenticated, setAuthenticated] = useState(false);

  if(!isAuthenticated) {
    return (
      <><Login setParentState={setAuthenticated} /></>
    )
  }

  return (
    <><Dashboard /></>
  );
}

export default App;
