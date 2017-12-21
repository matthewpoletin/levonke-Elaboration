package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.repository.ProjectRepository;
import com.levonke.Elaboration.web.model.ProjectRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
	
	private final ProjectRepository projectRepository;
	
	@Autowired
	public ProjectServiceImpl(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Project> getProjects(Integer page, Integer size) {
		return projectRepository.findAll(PageRequest.of(page, size));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Project> getProjectsWithName(String name, Integer page, Integer size) {
		return projectRepository.getProjectsByNameContainingIgnoreCase(name, PageRequest.of(page, size));
	}
	
	@Override
	@Transactional
	public Project createProject(ProjectRequest projectRequest) {
		Project project = new Project()
			.setName(projectRequest.getName())
			.setOfficialName(projectRequest.getOfficialName())
			.setDescription(projectRequest.getDescription())
			.setWebsite(projectRequest.getWebsite());
		projectRepository.save(project);
		this.setTeamToProject(project.getId(), project.getTeamId());
		return projectRepository.save(project);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Project getProjectById(Integer projectId) {
		return projectRepository.findById(projectId)
			.orElseThrow(() -> new EntityNotFoundException("Project '{" + projectId + "}' not found"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Project getProjectByName(String name) {
		return projectRepository.getProjectByNameIgnoreCase(name);
	}
	
	@Override
	@Transactional
	public Project updateProjectById(Integer projectId, ProjectRequest projectRequest) {
		Project project = this.getProjectById(projectId);
		project.setName(projectRequest.getName() != null ? projectRequest.getName() : project.getName());
		project.setOfficialName(projectRequest.getOfficialName() != null ? projectRequest.getOfficialName() : project.getOfficialName());
		project.setDescription(projectRequest.getDescription() != null ? projectRequest.getDescription() : project.getDescription());
		project.setWebsite(projectRequest.getWebsite() != null ? projectRequest.getWebsite() : project.getWebsite());
		this.setTeamToProject(projectId ,projectRequest.getTeamId());
		return projectRepository.save(project);
	}
	
	@Override
	@Transactional
	public void deleteProjectById(Integer projectId) {
		projectRepository.deleteById(projectId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Project> getProjectsOfTeam(Integer teamId) {
		return projectRepository.findByTeamId(teamId);
	}
	
	@Override
	@Transactional
	public Project setTeamToProject(Integer projectId, @NotNull Integer teamId) {
		if (teamId != null) {
			Project project = this.getProjectById(projectId);
			project.setTeamId(teamId);
			return projectRepository.save(project);
		}
		else return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Integer getTeamOfProject(Integer projectId, Integer teamId) {
		Project project = this.getProjectById(projectId);
		return project.getTeamId();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Version> getVersionsOfProject(Integer projectId) {
		return new ArrayList(this.getProjectById(projectId).getVersions());
	}

}
