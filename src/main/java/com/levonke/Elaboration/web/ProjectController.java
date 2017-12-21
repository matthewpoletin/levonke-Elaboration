package com.levonke.Elaboration.web;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.service.ProjectServiceImpl;
import com.levonke.Elaboration.web.model.ProjectRequest;
import com.levonke.Elaboration.web.model.ProjectResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(ProjectController.projectBaseURI)
public class ProjectController {
	
	static final String projectBaseURI = "/api/elaboration";
	
	private ProjectServiceImpl projectService;
	
	@Autowired
	public ProjectController(ProjectServiceImpl projectService) {
		this.projectService = projectService;
	}
	
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public Page<ProjectResponse> getProjects(@RequestParam(value = "page", required = false) Integer page,
											 @RequestParam(value = "size", required = false) Integer size,
											 @RequestParam(value = "name", required = false) String name) {
		page = page != null ? page : 0;
		size = size != null ? size : 25;
		Page<Project> projects;
		if (name != null) {
			projects = projectService.getProjectsWithName(name, page, size);
		} else {
			projects = projectService.getProjects(page, size);
		}
		return projects
			.map(ProjectResponse::new);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/projects", method = RequestMethod.POST)
	public ProjectResponse createProject(@Valid @RequestBody ProjectRequest projectRequest, HttpServletResponse response) {
		Project project = projectService.createProject(projectRequest);
		response.addHeader(HttpHeaders.LOCATION, projectBaseURI + "/projects/" + project.getId());
		return new ProjectResponse(project);
	}
	
	@RequestMapping(value = "/projects/{projectId}", method = RequestMethod.GET)
	public ProjectResponse getProjectById(@PathVariable("projectId") final Integer projectId) {
		return new ProjectResponse(projectService.getProjectById(projectId));
	}
	
	@RequestMapping(value = "/projects/by", method = RequestMethod.GET)
	public ProjectResponse getProjectBy(@RequestParam(value = "name") String name,
										HttpServletResponse response) {
		if (name != null && name.length() > 0) {
			return new ProjectResponse(projectService.getProjectByName(name));
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}

	@RequestMapping(value = "/projects/{projectId}", method = RequestMethod.PATCH)
	public ProjectResponse updateProjectById(@PathVariable("projectId") final Integer projectId,
											 @Valid @RequestBody ProjectRequest projectRequest) {
		return new ProjectResponse(projectService.updateProjectById(projectId, projectRequest));
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/projects/{projectId}", method = RequestMethod.DELETE)
	public void deleteProjectById(@PathVariable("projectId") final Integer projectId) {
		projectService.deleteProjectById(projectId);
	}
	
	@RequestMapping(value = "/projects/{projectId}/teams/{teamId}", method = RequestMethod.POST)
	public ProjectResponse setTeamToProject(@PathVariable("projectId") final Integer projectId,
											@PathVariable("teamId") final Integer teamId) {
		return new ProjectResponse(projectService.setTeamToProject(projectId, teamId));
	}
	
	@RequestMapping(value = "/projects/{projectId}/teams/{teamId}", method = RequestMethod.GET)
	public Integer getTeamOfProject(@PathVariable("projectId") final Integer projectId,
									@PathVariable("teamId") final Integer teamId) {
		return projectService.getTeamOfProject(projectId, teamId);
	}
	
	@RequestMapping(value = "/projects/{projectId}/versions", method = RequestMethod.GET)
	public List<Integer> getProjectVersions(@PathVariable("projectId") final Integer projectId) {
		List<Integer> versionsId = new ArrayList<>();
		projectService.getVersionsOfProject(projectId).forEach(version -> versionsId.add(version.getId()));
		return versionsId;
	}
	
}
