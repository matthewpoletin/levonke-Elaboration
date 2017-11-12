package com.levonke.Elaboration.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.persistence.*;

@Data
@Entity
@Accessors(chain = true)
@Table(	name = "versions", schema = "elaboration",
		uniqueConstraints = @UniqueConstraint(columnNames = {"versions_major", "versions_project_id"})
)
public class Version {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "versions_id")
	private Integer id;
	
	@Column(name = "versions_major")
	private Integer major;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "versions_project_id")
	private Project project;
	
	// TODO: Add support on subversionizing
	
	// TODO: Implement turning versin info into string
//	@Transient
//	private String number;
	
}
