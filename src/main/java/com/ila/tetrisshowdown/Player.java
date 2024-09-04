package com.ila.tetrisshowdown;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Player {
	@Id
	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String playerName;
	
	private String password;
	
	private float rating;
	
	private boolean availability;
}


// id, username, password, email, rating, availability