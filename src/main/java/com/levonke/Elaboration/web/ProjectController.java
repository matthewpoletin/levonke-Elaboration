package com.levonke.Elaboration.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.service.ProjectServiceImpl;
import com.levonke.Elaboration.web.model.ProjectRequest;
import com.levonke.Elaboration.web.model.ProjectResponse;

@RestController
@RequestMapping(ProjectController.PROJECT_BASE_URI)
public class ProjectController {

	public static final String PROJECT_BASE_URI = "/api/elaboration/projects";

	private ProjectServiceImpl projectService;
	
	@Autowired
	public ProjectController(ProjectServiceImpl projectService) {
		this.projectService = projectService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<ProjectResponse> getProjects(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "teamId", required = false) Integer teamId) {
		if (teamId != null) {
			return projectService.getProjectsByTeam(teamId, page != null ? page : 0, size != null ? size : 25)
				.stream()
				.map(ProjectResponse::new)
				.collect(Collectors.toList());
		}
		return projectService.getProjects(page != null ? page : 0, size != null ? size : 25)
			.stream()
			.map(ProjectResponse::new)
			.collect(Collectors.toList());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method = RequestMethod.POST)
	public void createProject(@RequestBody ProjectRequest projectRequest, HttpServletResponse response) {
		Project project = projectService.create(projectRequest);
		response.addHeader(HttpHeaders.LOCATION, this.PROJECT_BASE_URI + "/" + project.getId());
	}

	@RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
	public ProjectResponse getProject(@PathVariable("projectId") final Integer projectId) {
		return new ProjectResponse(projectService.read(projectId));
	}

	@RequestMapping(value = "/{projectId}", method = RequestMethod.PATCH)
	public ProjectResponse updateProject(@PathVariable("projectId") final Integer projectId, @RequestBody ProjectRequest projectRequest) {
		return new ProjectResponse(projectService.update(projectId, projectRequest));
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{projectId}", method = RequestMethod.DELETE)
	public void deleteProject(@PathVariable("projectId") final Integer projectId)
	{
		projectService.delete(projectId);
	}

}
