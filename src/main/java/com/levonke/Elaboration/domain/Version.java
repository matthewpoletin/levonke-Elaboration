package com.levonke.Elaboration.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

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
	
	@ManyToMany(fetch = FetchType.EAGER/*cascade = {CascadeType.PERSIST, CascadeType.MERGE}*/)
	@JoinTable(
			name = "versions_components",
			joinColumns = @JoinColumn(name = "versions_versions_id", referencedColumnName = "versions_id"),
			inverseJoinColumns = @JoinColumn(name = "componetns_components_id", referencedColumnName = "components_id")
	)
	private Collection<Component> components = new ArrayList<>();
	
	// TODO: Add support on subversionizing
	
	// TODO: Implement turning version info into string
//	@Transient
//	private String number;
	
}
