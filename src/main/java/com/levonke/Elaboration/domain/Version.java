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

	// TODO: add subverionizing
//	@Column(name = "versions_minor")
//	private Integer minor;
//
//	@Column(name = "versions_release")
//	private Integer release;
//
//	@Column(name = "versions_build")
//	private Integer build;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "versions_project_id")
	private Project project;

	@Transient
	private String number;

//	private String verionToString() {
//		return new String(major.toString() + "." + minor.toString() + "." + release.toString() + "." + build.toString());
//	}



}
