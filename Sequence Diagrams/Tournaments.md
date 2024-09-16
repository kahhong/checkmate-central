# Create a new tournament
```mermaid
sequenceDiagram
    actor Admin
    Admin->>Web UI: Clicks "Create Tournament"
    Web UI->>+TournamentController: POST /tournaments <br> {"name": ...}
    TournamentController->>+TournamentService: create(TournamentCreateForm)
    TournamentService->>+TournamentRepository: save(newTournament)
%%    TournamentRepository->>+H2 Database: INSERT INTO TOURNAMENT ... 
    TournamentRepository-->>-TournamentService: return newTournament
    TournamentService-->>-TournamentController: Saved Tournament
    TournamentController-->>-Web UI: 201 Created
    Web UI-->>Admin: Redirect to Tournament Management Page
```
# Browse all tournaments
```mermaid
sequenceDiagram
    actor User
    User->>Web UI: List of documents
    Web UI->>+TournamentController: GET /tournaments
    activate TournamentService
    TournamentController->>+TournamentService: findAll(id)
    TournamentService-->>-TournamentController: List<Tournament> response
    TournamentController-->>Web UI: 200 OK
    Web UI-->>User: Display a list of tournaments
```

# Get details of a specific tournament (Non-admin)
```mermaid
sequenceDiagram
    actor User
    User->>Web UI: Clicks a tournament
    Web UI->>+TournamentController: GET /tournaments/{id}
    activate TournamentService
    TournamentController->>+TournamentService: findById(id)
    TournamentService-->>-TournamentController: Optional<Tournament> tournament
    alt tournament.isPresent()
        TournamentController-->>Web UI: 200 OK
        Web UI-->>User: Show brief details of the tournament<br/>(name, description, min elo)
    else !tournament.isPresent()
        TournamentController-->>-Web UI: 404 Not Found
        Web UI-->>User: Show 404 error
    end
```

# Get details of a specific tournament (Admin)
> TODO: Check if admin is authorized to access the tournament.
```mermaid
sequenceDiagram
    actor Admin
    Admin->>Web UI: Clicks a tournament
    Web UI->>+TournamentController: GET /tournaments/{id}
    activate TournamentService
    TournamentController->>+TournamentService: findById(id)
    TournamentService-->>-TournamentController: Optional<Tournament> tournament
    alt tournament.isPresent()
        TournamentController-->>Web UI: 200 OK
        Web UI-->>Admin: Show full details of the tournament
    else !tournament.isPresent()
        TournamentController-->>-Web UI: 404 Not Found
        Web UI-->>Admin: Show 404 error
    end
```

# Update a tournament
> TODO: Check if admin is authorized to edit the tournament.
```mermaid
sequenceDiagram
    actor Admin
    Admin->>Web UI: Clicks "Save" after editing the tournament
    Web UI->>+TournamentController: PUT /tournaments/{id}<br/>{updated options}
    activate TournamentService
    TournamentController->>+TournamentService: findById(id)
    TournamentService-->>TournamentController: tournament
    deactivate TournamentService
    alt tournament.isPresent()
        alt tournament.Status == UPCOMING
            TournamentController->>+TournamentService: update(id, options)
            TournamentService->>+TournamentRepository: save(updatedTournament)
            activate TournamentRepository
            TournamentRepository-->>TournamentService: Tournament updatedTournament
            deactivate TournamentRepository
            TournamentService-->>-TournamentController: Tournament updatedTournament
            TournamentController-->>Web UI: 200 OK
            Web UI-->>Admin: Show success message
        else tournament.Status != UPCOMING
            TournamentController-->>Web UI: 405 Not Allowed
            Web UI-->>Admin: Show error message
        end
    else !tournament.isPresent()
        TournamentController-->>-Web UI: 404 Not Found
        Web UI-->>Admin: Show error message
    end
```

# Delete a tournament
```mermaid
sequenceDiagram
    actor Admin
    Admin->>Web UI: Clicks "Delete Tournament"
    Web UI->>+TournamentController: DELETE /tournaments/{id}
    activate TournamentService
    TournamentController->>+TournamentService: findById(id)
    TournamentService-->>TournamentController: tournament
    deactivate TournamentService
    alt tournament.isPresent()
        alt tournament.Status == UPCOMING
            TournamentController->>+TournamentService: delete(id)
            TournamentService->>+TournamentRepository: deleteById(id)
            deactivate TournamentService
%%            TournamentRepository->>H2 Database: DELETE FROM TOURNAMENT ...
%%            TournamentRepository-->>-TournamentService: void
%%            TournamentService-->>-TournamentController: void
            TournamentController-->>Web UI: 200 OK
            Web UI-->>Admin: Show success message and <br/> redirect to Tournament Management Page
        else tournament.Status != UPCOMING
            TournamentController-->>Web UI: 405 Not Allowed
            Web UI-->>Admin: Show error message and <br/> redirect to Tournament Management Page
        end
    else !tournament.isPresent()
        TournamentController-->>-Web UI: 404 Not Found
        Web UI-->>Admin: Show error message and <br/> redirect to Tournament Management Page
    end
```