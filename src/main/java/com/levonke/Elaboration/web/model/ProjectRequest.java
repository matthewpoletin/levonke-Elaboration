package com.levonke.Elaboration.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectRequest {
	private String name;
	private String description;
	private String website;
	private Integer teamId;
}
