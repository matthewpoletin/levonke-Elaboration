package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.web.model.VersionRequest;

import java.util.List;

public interface VersionService {
	List<Version> getVersions(Integer page, Integer size);
	Version create(VersionRequest versionRequest);
	Version read(Integer versionId);
	Version update(Integer versionId, VersionRequest versionRequest);
	void delete(Integer versionId);
}
