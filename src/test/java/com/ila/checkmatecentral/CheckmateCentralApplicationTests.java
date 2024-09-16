package com.ila.checkmatecentral;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.repository.TournamentRepository;

@SpringBootTest
class CheckmateCentralApplicationTests {

	@Autowired
	private TournamentRepository tournamentRepository;

	@Test
	void testTournamentRepository() {
		Tournament t = new Tournament();
		t.setName("Test");
		t.setDescription("Test Description");
		t.setCreateDate(LocalDateTime.now());
		this.tournamentRepository.save(t);
	}

	/* 
	@Test
	void contextLoads() {
	}
	*/

}
