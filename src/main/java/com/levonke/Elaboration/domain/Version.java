package com.levonke.Elaboration.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.persistence.*;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "versions", schema = "elaboration")
public class Version {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "versions_id")
	private Integer id;

	@Column(name = "versions_major")
	private Integer major;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "versions_project_id")
	private Project project;

	@Transient
	private String number;

}
