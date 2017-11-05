package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.web.model.ProjectRequest;

import java.util.List;

public interface ProjectService {
	List<Project> getProjects(Integer page, Integer size);
	Project create(ProjectRequest projectRequest);
	Project read(Integer projectId);
	Project update(Integer projectId, ProjectRequest projectRequest);
	void delete(Integer projectId);
}
