package com.levonke.Elaboration.service;

import com.levonke.Elaboration.domain.Component;
import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.repository.ComponentRepository;
import com.levonke.Elaboration.repository.VersionRepository;
import com.levonke.Elaboration.web.model.ComponentRequest;
import com.levonke.Elaboration.web.model.VersionRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VersionServiceImpl implements VersionService {
	
	private final VersionRepository versionRepository;
	
	private final ProjectServiceImpl projectService;
	
	private final ComponentRepository componentRepository;
	
	@Autowired
	public VersionServiceImpl(VersionRepository versionRepository, ProjectServiceImpl projectService, ComponentRepository componentRepository) {
		this.versionRepository = versionRepository;
		this.projectService = projectService;
		this.componentRepository = componentRepository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Version> getVersions(Integer page, Integer size) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}
		return versionRepository.findAll(PageRequest.of(page, size)).getContent();
	}
	
	@Override
	@Transactional
	public Version createVersion(VersionRequest versionRequest) {
		Version version = new Version()
			.setMajor(versionRequest.getMajor());
		versionRepository.save(version);
		this.setProjectToVersion(version.getId(), versionRequest.getProjectId());
		return versionRepository.save(version);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Version getVersionById(Integer versionId) {
		return versionRepository.findById(versionId)
			.orElseThrow(() -> new EntityNotFoundException("Version '{" + versionId + "}' not found"));
	}
	
	@Override
	@Transactional
	public Version updateVersionById(Integer versionId, VersionRequest versionRequest) {
		Version version = this.getVersionById(versionId);
		version.setMajor(versionRequest.getMajor() != null ? versionRequest.getMajor() : version.getMajor());
		this.setProjectToVersion(versionId, versionRequest.getProjectId());
		return versionRepository.save(version);
	}

	@Override
	@Transactional
	public void deleteVersionById(Integer versionId) {
		versionRepository.deleteById(versionId);
	}
	
	@Override
	@Transactional
	public void setProjectToVersion(Integer versionId, Integer projectId) {
		Version version = this.getVersionById(versionId);
		version.setProject(projectService.getProjectById(projectId));
		versionRepository.save(version);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Project getProjectOfVersion(Integer versionId) {
		return this.getVersionById(versionId).getProject();
	}
	
	@Override
	@Transactional
	public void addComponentsToVersion(Integer versionId, ComponentRequest componentRequest) {
		Version version = this.getVersionById(versionId);
		UUID uuid = UUID.fromString(componentRequest.getUuid());
		Component existingComponent = this.componentRepository.findComponentByUuid(uuid);
		if (existingComponent != null) {
			version.getComponents().add(existingComponent);
		}
		else {
			Component newComponent = new Component()
				.setUuid(uuid);
			componentRepository.save(newComponent);
			version.getComponents().add(newComponent);
		}
		versionRepository.save(version);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UUID> getComponentsOfVersion(Integer versionId) {
		Version version = this.getVersionById(versionId);
		List<UUID> componentsUuid = new ArrayList<>();
		version.getComponents().forEach(component -> componentsUuid.add(component.getUuid()));
		return componentsUuid;
	}
	
}
