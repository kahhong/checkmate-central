package com.ila.tetrisshowdown.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Admin {
	@Id
	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String name;
	
	private String password;
}
