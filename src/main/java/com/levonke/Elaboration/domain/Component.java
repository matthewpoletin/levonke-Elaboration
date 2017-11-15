package com.levonke.Elaboration.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "components", schema = "elaboration")
public class Component {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "components_id")
	private Integer id;
	
	@Column(name ="components_UUID")
	private UUID uuid;
	
	@ManyToMany(mappedBy = "components", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Collection<Version> versions = new ArrayList<>();
	
	
}
