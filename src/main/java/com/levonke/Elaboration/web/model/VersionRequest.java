package com.levonke.Elaboration.web.model;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class VersionRequest {

	@NotNull(message = "Not valid major")
	Integer major;
	
	Integer projectId;

}
