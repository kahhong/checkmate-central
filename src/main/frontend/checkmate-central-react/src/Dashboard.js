import { useState } from "react";
import { SERVER_URL } from "./env";

const Table = ({data}) => {
  const headers = [
    "name",
    "description",
    "maxPlayers",
    "minElo"
  ];

  return (
    <div>
      <table>
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

  async function getTournaments() {
    const url = SERVER_URL + "/api/tournaments/list";
    const requestHeader = {
      'content-type': 'application/json',
      'Authorization': 'Bearer ' + localStorage.accessToken
    }

    try {

      const response = await fetch(url, {
        headers: requestHeader,
        method: 'GET'
      });
      console.log(response.json);

    } catch (error) {
      console.error(error.message);
    }

  }

  
  return (
    <div>
      <div className="hero-title container">
        <div className="row justify-content-center">
          <h1>Checkmate Central</h1>
        </div>
      </div>
      {getTournaments()}
      <Table data={data} />

    </div>
  );
}

export default Dashboard;
