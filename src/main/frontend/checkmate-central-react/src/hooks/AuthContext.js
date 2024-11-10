import {createContext, useContext, useMemo, useState} from "react";
import {useNavigate} from "react-router-dom";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const navigate = useNavigate();
  const [userContext, setUserContext] = useState({
    userName: window.localStorage.getItem('userName'),
    token: window.localStorage.getItem('accessToken'),
    expiry: window.localStorage.getItem('tokenExpiry')
  });

  const setLogin = async (data) => {
    setUserContext(data);
  };

  const logout = () => {
    setUserContext(null);
    ['accessToken', 'tokenExpiry', 'userName'].map(e => window.localStorage.removeItem(e));
    navigate('/login', {replace: true});
  }

  const value = useMemo(
    () => ({
      userContext,
      setLogin,
      logout
    }),
    [userContext]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  return useContext(AuthContext);
};