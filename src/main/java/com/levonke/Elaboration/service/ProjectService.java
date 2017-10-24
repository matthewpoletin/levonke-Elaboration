package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.web.model.ProjectRequest;

public interface ProjectService {

	Iterable<Project> getProjects();
	Project create(ProjectRequest projectRequest);
	Project read(Integer projectId);
	Project update(Integer projectId, ProjectRequest projectRequest);
	void delete(Integer projectId);

}
