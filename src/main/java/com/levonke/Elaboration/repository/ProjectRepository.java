package com.levonke.Elaboration.repository;

import com.levonke.Elaboration.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository
		extends JpaRepository<Project, Integer> {
}
