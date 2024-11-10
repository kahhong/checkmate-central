import {useEffect, useState} from "react";
import { SERVER_URL } from "./env";
import './index.css';
import './vendor/bootstrap/css/bootstrap.min.css'

import Button from './components/Button'
import {Container} from "react-bootstrap";

import {MyNavbar} from "./components/MyNavbar";
const Table = ({data}) => {
  const dataHeaders = [
    "name",
    "description",
    "maxPlayers",
    "minElo"
  ];

  const tableHeaders = [
      "Name",
      "Description",
      "Capacity",
      "Minimum Elo"
  ]

  const handleJoin = (event) => {
    event.preventDefault();

    const tournamentId = event.target.value;
    const url = `${SERVER_URL}/api/tournaments/${tournamentId}/add`;
    const requestHeader = {
      'content-type': 'application/json',
      'Authorization': 'Bearer ' + localStorage.accessToken
    }

    const requestBody = {
      email: localStorage.userName
    }
    fetch(url, {
      headers: requestHeader,
      method: 'PUT',
      body: JSON.stringify(requestBody)
    })
      .then(response => response.json());
  }

  return (
    <div className="container">
      <table className="table table-striped">
        <thead>
          <tr>
            {tableHeaders.map(head => (
              <th>{head}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map(row => (
            <tr>
              {dataHeaders.map(head => {
                if (head === 'maxPlayers') {
                  return <td>{row['playerList'].length + "/" + row[head]}</td>
                }
                return <td>{row[head]}</td>
                }
              )}
              <td><Button children="Join" onClick={handleJoin} value={row['tournamentId']} disabled={false}/></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

function Dashboard() {
  let [data, setData] = useState([]);

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
  }, [])


  return (
    <>
      <MyNavbar></MyNavbar>

      <Container className="hero-title">
        <h1>List of Tournaments</h1>
      </Container>

      <Table data={data}/>
    </>
  )
}



export default Dashboard;
