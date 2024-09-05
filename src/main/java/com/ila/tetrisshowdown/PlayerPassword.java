package com.ila.tetrisshowdown;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlayerPassword {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="player_password")
	private Integer id;
    private String password; 

	// Constructor to set default values
    public PlayerPassword() {
        this.password = ""; 
    }

    public String getPassword() { 
        return this.password; 
    }

    @OneToOne
    @JoinColumn(name="player_id")
    private Player player; 
}
