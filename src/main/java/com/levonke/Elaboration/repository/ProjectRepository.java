package com.levonke.Elaboration.repository;

import com.levonke.Elaboration.domain.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository
		extends CrudRepository<Project, Integer> {
}
