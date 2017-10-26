package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.repository.VersionRepository;
import com.levonke.Elaboration.web.model.VersionRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class VersionServiceImpl implements VersionService {

	private final VersionRepository versionRepository;

	@Autowired
	public VersionServiceImpl(VersionRepository versionRepository) {
		this.versionRepository = versionRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Version> getVersions() {
		return versionRepository.findAll();
	}

	@Override
	@Transactional
	public Version create(VersionRequest versionRequest) {
		Version version = new Version()
			.setMajor(versionRequest.getMajor());
		// TODO: Add finding project by id;
//		version.setProject(project.getProjectId();
		return versionRepository.save(version);
	}

	@Override
	@Transactional(readOnly = true)
	public Version read(Integer versionId) {
		Version version = versionRepository.findOne(versionId);
		if (version == null) {
			throw new EntityNotFoundException("Version '{" + versionId + "}' not found");
		}
		return version;
	}

	@Override
	@Transactional
	public Version update(Integer versionId, VersionRequest versionRequest) {
		Version version = versionRepository.findOne(versionId);
		if (version == null) {
			throw new EntityNotFoundException("Version '{" + versionId + "}' not found");
		}
		version.setMajor(versionRequest.getMajor() != null ? versionRequest.getMajor() : version.getMajor());
		// TODO: Add finding associated project by projectId
		return versionRepository.save(version);
	}

	@Override
	@Transactional
	public void delete(Integer versionId) {
		versionRepository.delete(versionId);
	}

}
