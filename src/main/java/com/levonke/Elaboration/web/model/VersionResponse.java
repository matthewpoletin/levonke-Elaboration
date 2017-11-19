package com.levonke.Elaboration.web.model;

import com.levonke.Elaboration.domain.Version;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class VersionResponse {

	Integer id;
	Integer major;
	Integer projectId;
	List<ComponentResponse> components = new ArrayList<>();

	public VersionResponse(Version version) {
		this.id = version.getId();
		this.major = version.getMajor();
		this.projectId = version.getProject() != null ? version.getProject().getId() : null;
		version.getComponents().forEach(component -> this.components.add(new ComponentResponse(component)));
	}

}
