import './App.css';
import Login from "./components/login/Login"
import {
  BrowserRouter as Router,
  Route,
  Switch,
} from "react-router-dom";
import Home from './components/home/Home';
import ProtectedRoute from "./components/Routes/ProtectedRoute"
import TasksList from './components/Tasks/TasksList';

function App() {
  return (

    <Router>
      <Switch>
        <Route exact path='/' component={Login} />
        <ProtectedRoute exact path='/projects' component={Home} />
        <ProtectedRoute path="/projects/:id/tasks" component={TasksList} />
      </Switch>
    </Router>
  );
}

export default App;
