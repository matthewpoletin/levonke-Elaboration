package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.repository.ProjectRepository;
import com.levonke.Elaboration.web.model.ProjectRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
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
	public List<Project> getProjects(Integer page, Integer size) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}
		return projectRepository.findAll(PageRequest.of(page, size)).getContent();
	}
	
	@Override
	@Transactional
	public Project createProject(ProjectRequest projectRequest) {
		Project project = new Project()
			.setName(projectRequest.getName())
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
	@Transactional
	public Project updateProjectById(Integer projectId, ProjectRequest projectRequest) {
		Project project = this.getProjectById(projectId);
		project.setName(projectRequest.getName() != null ? projectRequest.getName() : project.getName());
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
	public Project setTeamToProject(Integer projectId, Integer teamId) {
		Project project = this.getProjectById(projectId);
		project.setTeamId(teamId);
		return projectRepository.save(project);
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
