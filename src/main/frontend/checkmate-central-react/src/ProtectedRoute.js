import {Navigate} from "react-router-dom";
import {useAuth} from "./hooks/AuthContext";

export const ProtectedRoute = ({ children }) => {
  const { userContext } = useAuth();
  if(!userContext.token) {
    return <Navigate to="/login" />;
  }

  return children;
};