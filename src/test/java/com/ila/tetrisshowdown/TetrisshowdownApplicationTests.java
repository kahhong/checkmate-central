package com.ila.tetrisshowdown;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ila.tetrisshowdown.repository.TournamentRepository;

@SpringBootTest
class TetrisshowdownApplicationTests {

	@Autowired
	private TournamentRepository tournamentRepository;

	@Test
	void contextLoads() {
		
	}

}
