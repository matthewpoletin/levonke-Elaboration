package com.levonke.Elaboration.web.model;

import com.levonke.Elaboration.domain.Component;
import lombok.Data;

@Data
public class ComponentResponse {
	
	String uuid;
	
	public ComponentResponse(String uuid) {
		this.uuid = uuid;
	}
	
	public ComponentResponse(Component component) {
		this.uuid = component.getUuid().toString();
	}
}
