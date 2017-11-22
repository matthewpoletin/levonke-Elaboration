package com.levonke.Elaboration.web;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.repository.VersionRepository;
import com.levonke.Elaboration.service.ProjectService;
import com.levonke.Elaboration.service.VersionServiceImpl;
import com.levonke.Elaboration.web.model.VersionRequest;
import com.levonke.Elaboration.web.model.VersionResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("VersionController Test")
class VersionControllerTest {
	
	private final Project project = new Project()
		.setId(1)
		.setName("Name")
		.setDescription("Description")
		.setWebsite("server.com")
		.setTeamId(1);
	
	private final Version version = new Version()
		.setId(1)
		.setMajor(1);
//		.setProject(Project);
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	private VersionServiceImpl versionService;
	
	@MockBean
	private VersionRepository versionRepositoryMock;

//	@MockBean
//	private ProjectService projectServiceMock;
	
	
	@BeforeAll
	void setUpMockMvc(WebApplicationContext wac) {
		mockMvc = webAppContextSetup(wac)
				.build();
	}
	
	@Test
	@DisplayName("Get versions")
	void getVersions() throws Exception {
		// Arrange
		List<Version> versions = new ArrayList<Version>() {{
			add(version);
		}};
		
		PageRequest pr = PageRequest.of(0, 25);
		PageImpl<Version> versionPage = new PageImpl<>(versions, pr, 100);
		
		when(versionRepositoryMock.findAll(any(Pageable.class))).thenReturn(versionPage);
		
		List<VersionResponse> expectedResponse = versions
			.stream()
			.map(VersionResponse::new)
			.collect(Collectors.toList());
		
		// Act
		MvcResult result = this.mockMvc.perform(
			get(VersionController.versionBaseURI + "/versions")
				.param("page", "0")
				.param("size", "25")
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$[0].id").exists())
			.andReturn();
		
		List<VersionResponse> actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<VersionResponse>>() { });
		
		// Assert
		assertEquals("Invalid version response", expectedResponse, actualResponse);
	}
	@Test
	@DisplayName("Create version")
	void createVersion() throws Exception {
		// Arrange
		Version versionNoId = new Version()
			.setMajor(1);
//			.setProject(project);

		when(versionRepositoryMock.save(versionNoId)).thenReturn(version);
		
		VersionRequest versionRequest = new VersionRequest()
			.setMajor(1);
//			.setProjectId(1);
		
		VersionResponse expectedResponse = new VersionResponse(version);
		
		// Act
		MvcResult result = this.mockMvc.perform(
			post(VersionController.versionBaseURI + "/versions")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(versionRequest))
			)
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", VersionController.versionBaseURI + "/versions" + "/1"))
			.andReturn();
		VersionResponse actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<VersionResponse>() { });
		
		// Assert
		verify(versionRepositoryMock, atLeast(1)).save(versionNoId);
		assertEquals("Invalid version response", expectedResponse, actualResponse);
	}
	
	@Test
	@DisplayName("Get version")
	void getVersion() throws Exception {
		// Arrange
		Optional<Version> versionOptional = Optional.of(version);
		
		when(versionRepositoryMock.findById(1)).thenReturn(versionOptional);
		
		// Act
		MvcResult result = this.mockMvc.perform(
				get(VersionController.versionBaseURI + "/versions" + "/1")
			)
			.andExpect(status().is2xxSuccessful())
			.andReturn();
		
		VersionResponse expectedResponse = new VersionResponse(version);
		VersionResponse actualResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<VersionResponse>() { });
		
		// Assert
		verify(versionRepositoryMock, times(1)).findById(1);
		assertEquals("Invalid version response", expectedResponse, actualResponse);
	}
	
	@Test
	@DisplayName("Update version")
	void updateVersion() throws Exception {
		// Arrange
		Version versionUpdated = new Version()
			.setId(1)
			.setMajor(2);
//			.setProject(project);
		
		Optional<Version> versionOptional = Optional.of(version);
		
		when(versionRepositoryMock.findById(1)).thenReturn(versionOptional);
		when(versionRepositoryMock.save(any(Version.class))).thenReturn(versionUpdated);
		
		VersionRequest versionRequest = new VersionRequest()
			.setMajor(2);
		
		// Act
		MvcResult result = this.mockMvc.perform(
			patch(VersionController.versionBaseURI + "/versions" + "/1")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(versionRequest))
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id").exists())
			.andReturn();
		
		VersionResponse expectedResponse = new VersionResponse(version);
		expectedResponse.setMajor(2);
		VersionResponse actualResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<VersionResponse>() { });
		
		// Assert
		verify(versionRepositoryMock, times(1)).findById(1);
		verify(versionRepositoryMock, times(1)).save(versionUpdated);
		assertEquals("Invalid version response", expectedResponse, actualResponse);
	}
	
	@Test
	@DisplayName("Delete version")
	void deleteVersion() throws Exception {
		// Act
		this.mockMvc.perform(
				delete(VersionController.versionBaseURI + "/versions" + "/1")
			)
			.andExpect(status().isNoContent());
		
		// Assert
		verify(versionRepositoryMock, times(1)).deleteById(1);
	}
	
}