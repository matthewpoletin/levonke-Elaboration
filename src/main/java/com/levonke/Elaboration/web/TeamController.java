package com.levonke.Elaboration.web;

import com.levonke.Elaboration.service.ProjectServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = TeamController.teamBaseURI)
public class TeamController {
	
	static final String teamBaseURI = "/api/elaboration/teams";
	
	private final ProjectServiceImpl projectService;
	
	@Autowired
	TeamController(ProjectServiceImpl projectService) {
		this.projectService = projectService;
	}
	
	@RequestMapping(value = "/{teamId}/projects")
	public List<Integer> GetProjectsOfTeam(@PathVariable(value = "teamId") final Integer teamId) {
		List<Integer> projectsId = new ArrayList<>();
		projectService.getProjectsOfTeam(teamId).forEach(project -> projectsId.add(project.getId()));
		return projectsId;
	}
	
}
