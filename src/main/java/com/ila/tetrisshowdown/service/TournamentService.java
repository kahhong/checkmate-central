package com.ila.tetrisshowdown.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ila.tetrisshowdown.entity.Admin;
import com.ila.tetrisshowdown.entity.Tournament;
import com.ila.tetrisshowdown.repository.TournamentRepository;
import com.ila.tetrisshowdown.exception.TournamentNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;

    public Page<Tournament> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.tournamentRepository.findAll(pageable);
    }

    public Tournament getTournament(Integer id) {
        Optional<Tournament> tournament = this.tournamentRepository.findById(id);
        if(tournament.isPresent()) {
            return tournament.get();
        } else {
            throw new TournamentNotFoundException("Tournament Not Found!!!");
        }
    }
    
    public void create(String title, String detail, Admin admin) {
        Tournament t = new Tournament();
        t.setTitle(title);
        t.setDetail(detail);
        t.setCreateDate(LocalDateTime.now());
        t.setAdmin(admin);
        this.tournamentRepository.save(t);
    }

    public void delete(Tournament tournament) {
        this.tournamentRepository.delete(tournament);
    }
}
