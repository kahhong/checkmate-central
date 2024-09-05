package com.ila.tetrisshowdown;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
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
	
	@Column(unique = true)
	private String email;
	
	private float rating;
	
	private boolean availability;
	
	// Constructor to set default values
    public Player() {
        this.rating = 0;
        this.availability = false;
    }

	@OneToOne(mappedBy="player", cascade= CascadeType.ALL)
	private PlayerPassword password; 
	
	public String getPassword() { 
		return this.password.getPassword(); 
	}
}


// id, username, password, email, rating, availability