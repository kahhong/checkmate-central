import './App.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "./Login";
import Dashboard from "./Dashboard";
import Profile from "./Profile";
import Register from "./Register";
import { ProtectedRoute } from "./ProtectedRoute";
import { AuthProvider } from "./hooks/AuthContext";
import CreateTournament from "./CreateTournament";
import { NotFoundPage } from "./404";

function App() {

  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<Login />}></Route>
          <Route path="/*" element={<NotFoundPage />}></Route>
          <Route path="/login" element={<Login />}></Route>
          <Route path="/register" element={<Register />}></Route>
          <Route path="/profile" element={<Profile />}></Route>
          <Route path="/dashboard" element={
            <ProtectedRoute children={<Dashboard />} />
          }></Route>
          <Route path="/createTournament" element={
            <ProtectedRoute children={<CreateTournament />} />
          }></Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
