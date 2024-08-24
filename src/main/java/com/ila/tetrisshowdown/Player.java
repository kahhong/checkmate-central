package com.ila.tetrisshowdown;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String userName;
	
	private String password;
	
	@Column(unique = true)
	private String email;
	
	private float rating;
	
	private boolean availability;
	
	
}


// id, username, password, email, rating, availability