package com.ila.checkmatecentral.config;

import com.ila.checkmatecentral.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({ HttpMessageConversionException.class })
    public ResponseEntity<?> handleHttpMessageConversionException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private static ResponseEntity<Object> handleException(Exception ex, String message) {
        return handleException(ex, message, HttpStatus.OK);
    }


    private static ResponseEntity<Object> handleException(Exception ex, String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(Map.of(
            "timestamp", LocalDateTime.now(),
            "status", httpStatus.value(),
            "error", "EXCEPTION: " + message,
            "message", ex.getMessage()
        ), httpStatus);
    }

    @ExceptionHandler(AccountExistsException.class)
    public ResponseEntity<Object> handleAccountExistsException(AccountExistsException ex, WebRequest request) {
        return handleException(ex, "Account already exists");
    }

    @ExceptionHandler(InvalidAvailabilityException.class)
    public ResponseEntity<Object> handleInvalidAvailabilityException(InvalidAvailabilityException ex, WebRequest request) {
        return handleException(ex, "Player is not available");
    }

    @ExceptionHandler(InvalidNumberOfPlayersException.class)
    public ResponseEntity<Object> handleInvalidNumberOfPlayersException(InvalidNumberOfPlayersException ex,
            WebRequest request) {
        return handleException(ex, "Invalid number of players");
    }

    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest request) {
        log.error("caught bad credentials error");
        return handleException(ex, "Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTournamentException.class)
    public ResponseEntity<Object> handleInvalidTournamentException(InvalidTournamentException ex, WebRequest request) {
        return handleException(ex, "Invalid tournament");
    }

    @ExceptionHandler(InvalidTournamentStateException.class)
    public ResponseEntity<Object> handleInvalidTournamentStateException(InvalidTournamentStateException ex, WebRequest request) {
        return handleException(ex, "Invalid tournament state");
    }

    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<Object> handleMatchNotFoundException(MatchNotFoundException ex, WebRequest request) {
        return handleException(ex, "Match not found");
    }

    @ExceptionHandler(PlayerAlreadyInTournamentException.class)
    public ResponseEntity<Object> handlePlayerAlreadyInTournamentException(PlayerAlreadyInTournamentException ex,
            WebRequest request) {
        return handleException(ex, "Player already in tournament");
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<Object> handlePlayerNotFoundException(PlayerNotFoundException ex,
            WebRequest request) {
        return handleException(ex, "Could not find player");
    }

    @ExceptionHandler(PlayerNotInTournamentException.class)
    public ResponseEntity<Object> handlePlayerNotInTournamentException(PlayerAlreadyInTournamentException ex,
            WebRequest request) {
        return handleException(ex, "Player already in tournament");
    }

    @ExceptionHandler(TournamentFullException.class)
    public ResponseEntity<Object> handleTournamentFullExceptionException(TournamentFullException ex, WebRequest request) {
        return handleException(ex, "Tournament is full");
    }

    @ExceptionHandler(TournamentNotFoundException.class)
    public ResponseEntity<Object> handleTournamentNotFoundExceptionException(TournamentNotFoundException ex,
            WebRequest request) {
        return handleException(ex, "Tournament not found");
    }
}
