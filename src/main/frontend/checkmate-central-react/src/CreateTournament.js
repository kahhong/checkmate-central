import { useState } from "react";
import { SERVER_URL } from "./env.js";
import { useNavigate } from "react-router-dom";


function CreateTournamentForm() {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [maxPlayers, setMaxPlayers] = useState('');
  const [minElo, setMinElo] = useState('');
  const [tournamentType, setTournamentType] = useState('SINGLE_KNOCKOUT');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');

  const navigate = useNavigate();

  async function createTournament(e) {
    e.preventDefault();


    const registerUrl = SERVER_URL + '/api/tournaments/';

    let requestBody = {
      name: name,
      description: description,
      maxPlayers: maxPlayers,
      minElo: minElo,
      type: tournamentType,
      startDate: startDate,
      endDate: endDate
    }
    
    const requestheader = {
      'content-type': 'application/json',
      'Authorization': "Bearer " + localStorage.accessToken
    }


    try{
      const response = await fetch(registerUrl, {
        headers: requestheader,
        body: JSON.stringify(requestBody),
        method: "POST"
      });

        if(response.status === 200) { navigate('/dashboard'); }

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


      <div className="container register-dialog">
        <form onSubmit={createTournament}>

          <div className="mb-3">
            <label htmlFor="tournamentName" className="form-label">Tournament Name</label>
            <input type="text" className="form-control" id="tournamentName" 
            value = {name} onChange={e => setName(e.target.value)}>
            </input>
          </div>
          
          <div className="mb-3">
            <label htmlFor="description" className="form-label">Tournament Description</label>
            <input type="text" className="form-control" id="description"
              value={description} onChange={e => setDescription(e.target.value)}></input>
          </div>

          <div className="mb-3">
            <label htmlFor="maxPlayers" className="form-label">Max Players</label>
            <input type="number" className="form-control" id="maxPlayers"
              value={maxPlayers} onChange={e => setMaxPlayers(e.target.value)}></input>
          </div>

          <div className="mb-3">
            <label htmlFor="minElo" className="form-label">Minimum Elo</label>
            <input type="number" className="form-control" id="minElo" value={minElo}
              onChange={e => setMinElo(e.target.value)}></input>
          </div>
          
          <div className="mb-3">
            <label htmlFor="tournamentType" className="form-label">Tournament Type</label>
            <br></br>
            <select id="tournamentType" name="type" defaultValue="SINGLE_KNOCKOUT"
              onChange={e => setTournamentType(e.target.value)}>

              <option value="SINGLE_KNOCKOUT">Single Knockout</option>
            </select>
          </div>

          <div className="mb-3">
            <label htmlFor="startDate" className="form-label">Start Date</label>
            <input type="datetime-local" className="form-control" id="startDate"
              value={startDate} onChange={e => setStartDate(e.target.value)}></input>
          </div>

          <div className="mb-3">
            <label htmlFor="endDate" className="form-label">End Date</label>
            <input type="datetime-local" className="form-control" id="endDate"
              value={endDate} onChange={e => setEndDate(e.target.value)}></input>
          </div>
          
          <button type="submit" className="btn btn-primary" id="create-button">Create</button>

        </form>
      </div>
      <footer>

      </footer>
    </div>
  );
}

export default CreateTournamentForm;
