import { useState } from "react";

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

  function getTournaments() {
    let xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
          if (this.readyState === XMLHttpRequest.DONE && this.status === 200)  {
            const data = JSON.parse(this.responseText);
            setData(data);
              
          } else if (this.readyState === XMLHttpRequest.DONE) {
          }
      };
  
      xhttp.open("GET", "http://localhost:8080/tournaments/list", true);
      xhttp.setRequestHeader('Content-type', 'application/json');
      xhttp.setRequestHeader('Authorization', 'Basic ' + btoa('admin1@example.com:password123'));
      xhttp.withCredentials = true;
      xhttp.send();
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
