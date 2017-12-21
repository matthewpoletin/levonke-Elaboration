package com.levonke.Elaboration.repository;

import com.levonke.Elaboration.domain.Project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository
		extends JpaRepository<Project, Integer> {
	
	Project getProjectByNameIgnoreCase(String name);
	
	List<Project> findByTeamId(Integer teamId);
	
	Page<Project> getProjectsByNameContainingIgnoreCase(String name, Pageable pageable);
	
}
