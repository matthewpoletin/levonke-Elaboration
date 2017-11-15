package com.levonke.Elaboration.repository;

import com.levonke.Elaboration.domain.Component;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ComponentRepository
		extends JpaRepository<Component, Integer> {
	
	Component findComponentByUuid(UUID uuid);
}
