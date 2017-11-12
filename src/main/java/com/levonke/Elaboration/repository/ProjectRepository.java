package com.levonke.Elaboration.repository;

import com.levonke.Elaboration.domain.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository
		extends JpaRepository<Project, Integer> {
	
	List<Project> findByTeamId(Integer teamId);
	
}
