package com.levonke.Elaboration.web.model;

import lombok.Data;

import com.levonke.Elaboration.domain.Version;

@Data
public class VersionResponse {

	Integer id;
	Integer major;
	Integer projectId;

	public VersionResponse(Version version) {
		this.id = version.getId();
		this.major = version.getMajor();
		this.projectId = version.getProject().getId();
	}

}
