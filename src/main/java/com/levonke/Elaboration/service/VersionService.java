package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.web.model.VersionRequest;

public interface VersionService {

	Iterable<Version> getVersions();
	Version create(VersionRequest versionRequest);
	Version read(Integer versionId);
	Version update(Integer versionId, VersionRequest versionRequest);
	void delete(Integer versionId);

}
