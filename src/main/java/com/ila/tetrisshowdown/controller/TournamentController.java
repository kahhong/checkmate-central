package com.ila.tetrisshowdown.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ila.tetrisshowdown.service.PlayerService;
import com.ila.tetrisshowdown.service.TournamentService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/tournament")
@RequiredArgsConstructor
@Controller
public class TournamentController {
    private final TournamentService TournamentService;
    private final PlayerService playerService;

    // Tournament List
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        return "tournament_list";
    }
}
