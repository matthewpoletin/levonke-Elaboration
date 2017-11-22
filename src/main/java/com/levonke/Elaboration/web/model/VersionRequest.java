package com.levonke.Elaboration.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VersionRequest {

	Integer major;
	Integer projectId;

}
