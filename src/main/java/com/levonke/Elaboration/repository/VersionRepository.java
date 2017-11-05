package com.levonke.Elaboration.repository;

import com.levonke.Elaboration.domain.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository
	extends JpaRepository<Version, Integer> {
}
