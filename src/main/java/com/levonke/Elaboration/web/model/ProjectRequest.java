package com.levonke.Elaboration.web.model;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
public class ProjectRequest {
	
	@NotEmpty(message = "Not valid name")
	private String name;
	
	private String description;
	
	private String website;
	
	private Integer teamId;
}
