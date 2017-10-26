package com.levonke.Elaboration.web;

import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.service.VersionServiceImpl;

import com.levonke.Elaboration.web.model.VersionRequest;
import com.levonke.Elaboration.web.model.VersionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
@RequestMapping(VersionController.VERSION_BASE_URI)
public class VersionController {

	public static final String VERSION_BASE_URI = "/versions";

	@Autowired
	VersionServiceImpl versionService;

	@RequestMapping(method = RequestMethod.GET)
	public ArrayList<VersionResponse> getVersions() {
		ArrayList<VersionResponse> versionResponses = new ArrayList<VersionResponse>();
		versionService.getVersions().forEach(version -> versionResponses.add(new VersionResponse(version)));
		return versionResponses;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method = RequestMethod.POST)
	public void createVersion(@RequestBody VersionRequest versionRequest, HttpServletResponse response) {
		Version version = versionService.create(versionRequest);
		response.addHeader(HttpHeaders.LOCATION, this.VERSION_BASE_URI + "/" + version.getId());
	}
	
	@RequestMapping(value = "/{verionId}", method = RequestMethod.GET)
	public VersionResponse readVersion (@PathVariable("versionId") Integer versionId) {
		return new VersionResponse(versionService.read(versionId));
	}

	@RequestMapping(value = "/{versionId}", method = RequestMethod.PATCH)
	public VersionResponse updateVersion(@PathVariable("versionId") Integer versionId, @RequestBody VersionRequest versionRequest) {
		return new VersionResponse(versionService.update(versionId, versionRequest));
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{versionId}", method = RequestMethod.DELETE)
	public void deleteVersion(@PathVariable("versionId") Integer versionId) {
		versionService.delete(versionId);
	}

}
