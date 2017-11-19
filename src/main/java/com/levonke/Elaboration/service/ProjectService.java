package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.web.model.ProjectRequest;
import java.util.List;

public interface ProjectService {
	List<Project> getProjects(Integer page, Integer size);
	Project createProject(ProjectRequest projectRequest);
	Project getProjectById(Integer projectId);
	Project updateProjectById(Integer projectId, ProjectRequest projectRequest);
	void deleteProjectById(Integer projectId);
	
	List<Project> getProjectsOfTeam(Integer teamId);
	
	Project setTeamToProject(Integer project, Integer teamId);
	Integer getTeamOfProject(Integer projectId, Integer teamId);
	
	List<Version> getVersionsOfProject(Integer projectId);
}
