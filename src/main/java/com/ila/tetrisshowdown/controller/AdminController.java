package com.ila.tetrisshowdown.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ila.tetrisshowdown.entity.Player;
import com.ila.tetrisshowdown.repository.PlayerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PlayerRepository playerRepository;

    @GetMapping("/playerList")
    public String list(Model model) {
        List<Player> playerList = this.playerRepository.findAll();
        model.addAttribute("playerList", playerList);
        return "player_list";
    }
}
