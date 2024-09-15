# Rank Calculation
```mermaid
sequenceDiagram
    actor Admin
    Admin->>+Web UI: Submit Match Outcome
    Web UI->>+MatchController: POST /match/outcome/ <br/> {"player": ...}
    
```