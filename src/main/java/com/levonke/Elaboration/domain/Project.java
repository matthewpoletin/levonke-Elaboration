package com.levonke.Elaboration.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "projects", schema = "elaboration")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "projects_id")
	private Integer id;
	
	@Column(name = "projects_name")
	private String name;
	
	@Column(name = "projects_description")
	private String description;
	
	@Column(name = "projects_website")
	private String website;
	
	@Column(name = "projects_team_id")
	private Integer teamId;
	
	@JsonIgnore
	@OneToMany(mappedBy = "project")
	private Collection<Version> versions = new ArrayList<>();
	
}
