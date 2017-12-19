package com.levonke.Elaboration.web.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;

@Data
public class ComponentRequest {
	
//	@NotEmpty(message = "Not valid UUID")
	@Length(min = 36, max = 36, message = "Not valid UUID")
	String uuid;
	
}
