package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.web.model.ComponentRequest;
import com.levonke.Elaboration.web.model.VersionRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface VersionService {
	Page<Version> getVersions(Integer page, Integer size);
	Version createVersion(VersionRequest versionRequest);
	Version getVersionById(Integer versionId);
	Version updateVersionById(Integer versionId, VersionRequest versionRequest);
	void deleteVersionById(Integer versionId);
	
	void setProjectToVersion(Integer versionId, Integer projectId);
	Project getProjectOfVersion(Integer versionId);
	
	void addComponentToVersion(Integer versionId, ComponentRequest componentRequest);
	List<UUID> getComponentsOfVersion(Integer versionId);
}
