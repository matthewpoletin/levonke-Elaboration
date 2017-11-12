package com.levonke.Elaboration.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.service.ProjectServiceImpl;
import com.levonke.Elaboration.web.model.ProjectRequest;
import com.levonke.Elaboration.web.model.ProjectResponse;

@RestController
@RequestMapping(ProjectController.projectBaseURI)
public class ProjectController {
	
	static final String projectBaseURI = "/api/elaboration/projects";
	
	private ProjectServiceImpl projectService;
	
	@Autowired
	public ProjectController(ProjectServiceImpl projectService) {
		this.projectService = projectService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<ProjectResponse> getProjects(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
		return projectService.getProjects(page != null ? page : 0, size != null ? size : 25)
			.stream()
			.map(ProjectResponse::new)
			.collect(Collectors.toList());
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method = RequestMethod.POST)
	public void createProject(@RequestBody ProjectRequest projectRequest, HttpServletResponse response) {
		Project project = projectService.createProject(projectRequest);
		response.addHeader(HttpHeaders.LOCATION, projectBaseURI + "/" + project.getId());
	}
	
	@RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
	public ProjectResponse getProject(@PathVariable("projectId") final Integer projectId) {
		return new ProjectResponse(projectService.getProjectById(projectId));
	}

	@RequestMapping(value = "/{projectId}", method = RequestMethod.PATCH)
	public ProjectResponse updateProject(@PathVariable("projectId") final Integer projectId, @RequestBody ProjectRequest projectRequest) {
		return new ProjectResponse(projectService.updateProjectById(projectId, projectRequest));
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{projectId}", method = RequestMethod.DELETE)
	public void deleteProject(@PathVariable("projectId") final Integer projectId) {
		projectService.deleteProjectById(projectId);
	}
	
	@RequestMapping(value = "/{projectId}/teams/{teamId}", method = RequestMethod.POST)
	public void setTeamToProject(@PathVariable("projectId") final Integer projectId, @PathVariable("projectId") final Integer teamId) {
		projectService.setTeamToProject(projectId, teamId);
	}
	
	@RequestMapping(value = "/{projectId}/teams/{teamId}", method = RequestMethod.GET)
	public Integer getTeamOfProject(@PathVariable("projectId") final Integer projectId, @PathVariable("projectId") final Integer teamId) {
		return projectService.getTeamOfProject(projectId, teamId);
	}
	
	@RequestMapping(value = "/{projectId}/versions", method = RequestMethod.GET)
	public List<Integer> getProjectVersions(@PathVariable("projectId") final Integer projectId) {
		List<Integer> versionsId = new ArrayList<>();
		projectService.getVersionsOfProject(projectId).forEach(version -> versionsId.add(version.getId()));
		return versionsId;
	}
	
}
