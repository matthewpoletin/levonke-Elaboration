package com.levonke.Elaboration.web.model;

import com.levonke.Elaboration.domain.Project;
import lombok.Data;

import com.levonke.Elaboration.domain.Version;

@Data
public class VersionResponse {

	Integer id;
	Integer major;
	Project project;

	public VersionResponse(Version version) {
		this.id = version.getId();
		this.major = version.getMajor();
		this.project = version.getProject();
	}

}
