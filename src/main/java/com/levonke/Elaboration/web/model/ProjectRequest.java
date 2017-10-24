package com.levonke.Elaboration.web.model;

import lombok.Data;

@Data
public class ProjectRequest {
	private String name;
	private String description;
	private String website;
	private Integer teamId;
}
