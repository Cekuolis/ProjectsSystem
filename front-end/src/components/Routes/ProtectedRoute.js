import { Redirect, Route } from 'react-router-dom'

export default function ProtectedRoute({ children, ...rest }) {

  const isAuthenticated = localStorage.getItem('token');

  return (
    <Route
      {...rest}
      render={() =>
        isAuthenticated ? (
          <>{children}</>
        ) : (
          <Redirect to="/" />
        )
      }
    ></Route>
  );
};