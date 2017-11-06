package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.repository.ProjectRepository;
import com.levonke.Elaboration.web.model.ProjectRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
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
		return projectRepository.findAll(new PageRequest(page, size)).getContent();
	}
	
	public List<Project> getProjectsByTeam(Integer teamId, Integer page, Integer size) {
		return projectRepository.findByTeamId(teamId, new PageRequest(page, size));
	}
	
	@Override
	@Transactional
	public Project create(ProjectRequest projectRequest) {
		Project project = new Project()
			.setName(projectRequest.getName())
			.setDescription(projectRequest.getDescription())
			.setWebsite(projectRequest.getWebsite())
			.setTeamId(projectRequest.getTeamId());
		return projectRepository.save(project);
	}

	@Override
	@Transactional(readOnly = true)
	public Project read(Integer projectId) {
		Project project = projectRepository.findOne(projectId);
		if (project == null) {
			throw new EntityNotFoundException("Project '{" + projectId + "}' not found");
		}
		return project;
	}

	@Override
	@Transactional
	public Project update(Integer projectId, ProjectRequest projectRequest) {
		Project project = projectRepository.findOne(projectId);
		if (project == null) {
			throw new EntityNotFoundException("Project '{" + projectId + "}' not found");
		}
		project.setName(projectRequest.getName() != null ? projectRequest.getName() : project.getName());
		project.setDescription(projectRequest.getDescription() != null ? projectRequest.getDescription() : project.getDescription());
		project.setWebsite(projectRequest.getWebsite() != null ? projectRequest.getWebsite() : project.getWebsite());
		// TODO: Add request to community service
		project.setTeamId(projectRequest.getTeamId() != null ? projectRequest.getTeamId() : project.getTeamId());
		return projectRepository.save(project);
	}

	@Override
	@Transactional
	public void delete(Integer projectId) {
		projectRepository.delete(projectId);
	}

}
