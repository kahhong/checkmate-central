import {useEffect, useState} from "react";
import { SERVER_URL } from "./env";
import './index.css';
import './vendor/bootstrap/css/bootstrap.min.css'

const Table = ({data}) => {
  const headers = [
    "name",
    "description",
    "maxPlayers",
    "minElo"
  ];

  return (
    <div>
      <table className="table table-striped">
        <thead>
          <tr>
            {headers.map(head => (
              <th>{head}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map(row => (
            <tr>
              {headers.map(head => (
                <td>{row[head]}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

function Dashboard() {
  const [data, setData] = useState([]);

  const url = SERVER_URL + "/api/tournaments/list";
  const requestHeader = {
    'content-type': 'application/json',
    'Authorization': 'Bearer ' + localStorage.accessToken
  }

  useEffect(() => {
    fetch(url, {
      headers: requestHeader,
      method: 'GET'
    }).then(response => response.json())
        .then(json => setData(json));
  }, []);

  
  return (
    <div>
      <div className="hero-title container">
        <div>
          <h1>Checkmate Central</h1>
        </div>
      </div>
      <div className="container">
        <Table data={data} />
      </div>
    </div>
  );
}

export default Dashboard;
