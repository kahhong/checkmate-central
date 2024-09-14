package com.ila.tetrisshowdown.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter
public class Tournament {
	@Id // Primary Key
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Integer id;
	
	@Column(length=200) 
	private String title;
	
	@Column(columnDefinition="Text") 
	private String detail;
	
	private LocalDateTime createDate;
	
	@OneToMany(mappedBy="tournament", cascade=CascadeType.REMOVE)
	private List<Player> playerList;
	
	@ManyToOne
	private Admin admin;
}
