package com.levonke.Elaboration.web;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.service.VersionServiceImpl;
import com.levonke.Elaboration.web.model.ComponentRequest;
import com.levonke.Elaboration.web.model.ComponentResponse;
import com.levonke.Elaboration.web.model.VersionRequest;
import com.levonke.Elaboration.web.model.VersionResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(VersionController.versionBaseURI)
public class VersionController {
	
	static final String versionBaseURI = "/api/elaboration/versions";
	
	private VersionServiceImpl versionService;
	
	@Autowired
	public VersionController(VersionServiceImpl versionService) {
		this.versionService = versionService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<VersionResponse> getVersions(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
		return versionService.getVersions(page, size)
			.stream()
			.map(VersionResponse::new)
			.collect(Collectors.toList());
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method = RequestMethod.POST)
	public void createVersion(@RequestBody VersionRequest versionRequest, HttpServletResponse response) {
		Version version = versionService.createVersion(versionRequest);
		response.addHeader(HttpHeaders.LOCATION, versionBaseURI + "/" + version.getId());
	}
	
	@RequestMapping(value = "/{versionId}", method = RequestMethod.GET)
	public VersionResponse readVersion (@PathVariable("versionId") final Integer versionId) {
		return new VersionResponse(versionService.getVersionById(versionId));
	}
	
	@RequestMapping(value = "/{versionId}", method = RequestMethod.PATCH)
	public VersionResponse updateVersion(@PathVariable("versionId") final Integer versionId, @RequestBody VersionRequest versionRequest) {
		return new VersionResponse(versionService.updateVersionById(versionId, versionRequest));
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{versionId}", method = RequestMethod.DELETE)
	public void deleteVersion(@PathVariable("versionId") Integer versionId) {
		versionService.deleteVersionById(versionId);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/{projectId}/versions/{versionId}", method = RequestMethod.POST)
	public void setProjectToVersion(@PathVariable("projectId") final Integer projectId, @PathVariable("versionId") final Integer versionId) {
		versionService.setProjectToVersion(versionId, projectId);
	}
	
	@RequestMapping(value = "/{versionId}/projects", method = RequestMethod.GET)
	public Project getProjectOfVersion(@PathVariable("versionId") final Integer versionId) {
		return versionService.getProjectOfVersion(versionId);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/{versionId}/components", method = RequestMethod.POST)
	public void addComponentToVersion(@PathVariable("versionId") final Integer versionId, @RequestBody ComponentRequest componentRequest) {
		versionService.addComponentsToVersion(versionId, componentRequest);
	}
	
	@RequestMapping(value = "/{versionId}/components", method = RequestMethod.GET)
	public List<ComponentResponse> getComponentsOfVersion(@PathVariable("versionId") final Integer versionId) {
		List<ComponentResponse> componentResponses = new ArrayList<>();
		versionService.getComponentsOfVersion(versionId).forEach(uuid -> componentResponses.add(new ComponentResponse(uuid.toString())));
		return componentResponses;
	}
	
}
