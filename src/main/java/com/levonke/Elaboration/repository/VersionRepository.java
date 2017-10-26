package com.levonke.Elaboration.repository;

import com.levonke.Elaboration.domain.Version;
import org.springframework.data.repository.CrudRepository;

public interface VersionRepository
	extends CrudRepository<Version, Integer> {
}
