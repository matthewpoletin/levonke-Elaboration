package com.levonke.Elaboration.web.model;

import com.levonke.Elaboration.domain.Project;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectResponse {

	private Integer id;
	private String name;
	private String officialName;
	private String description;
	private String website;
	private Integer teamId;

	public ProjectResponse(Project project) {
		this.id = project.getId();
		this.name = project.getName();
		this.officialName = project.getName();
		this.description = project.getDescription();
		this.website = project.getWebsite();
		this.teamId = project.getTeamId();
	}

}
