package com.ila.tetrisshowdown.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ila.tetrisshowdown.entity.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
	Tournament findBySubject(String subject);
	List<Tournament> findBySubjectLike(String subject);
	Page<Tournament> findAll(Specification<Tournament> spec, Pageable pageable); 
}
