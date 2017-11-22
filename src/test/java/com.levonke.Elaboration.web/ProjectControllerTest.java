package com.levonke.Elaboration.web;

import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.domain.Project;
import com.levonke.Elaboration.domain.Version;
import com.levonke.Elaboration.repository.ProjectRepository;
import com.levonke.Elaboration.service.ProjectService;
import com.levonke.Elaboration.service.ProjectServiceImpl;
import com.levonke.Elaboration.web.model.ProjectRequest;
import com.levonke.Elaboration.web.model.ProjectResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
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
@DisplayName("ProjectController Test")
class ProjectControllerTest {
	
	private final Project project = new Project()
		.setId(1)
		.setName("Name")
		.setDescription("Description")
		.setWebsite("server.com");
//		.setTeamId(1);
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	private ProjectServiceImpl projectService;
	
	@MockBean
	private ProjectRepository projectRepositoryMock;
	
	@BeforeAll
	void setUpMockMvc(WebApplicationContext wac) {
		mockMvc = webAppContextSetup(wac)
			.build();
	}
	
	@Test
	@DisplayName("Get projects")
	void getProjects() throws Exception {
		// Arrange
		List<Project> projects = new ArrayList<Project>() {{
			add(project);
		}};
		
		PageRequest pr = PageRequest.of(0, 25);
		PageImpl<Project> projectPage = new PageImpl<>(projects, pr, 100);
		
		when(projectRepositoryMock.findAll(any(Pageable.class))).thenReturn(projectPage);
		
		List<ProjectResponse> expectedResponse = projects
			.stream()
			.map(ProjectResponse::new)
			.collect(Collectors.toList());
		
		// Act
		MvcResult result = this.mockMvc.perform(
			get(ProjectController.projectBaseURI + "/projects")
				.param("page", "0")
				.param("size", "25")
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$[0].id").exists())
			.andReturn();
		
		List<ProjectResponse> actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ProjectResponse>>() { });
		
		// Assert
		assertEquals("Invalid project response", expectedResponse, actualResponse);
	}
	@Test
	@DisplayName("Create project")
	void createProject() throws Exception {
		// Arrange
		Project projectNoId = new Project()
				.setName("Name")
				.setDescription("Description")
				.setWebsite("server.com");
//				.setTeamId(1);
		
		when(projectRepositoryMock.save(projectNoId)).thenReturn(project);
		
		ProjectRequest projectRequest = new ProjectRequest()
			.setName("Name")
			.setDescription("Description")
			.setWebsite("server.com");
//			.setTeamId(1);
		
		// Act
		MvcResult result = this.mockMvc.perform(
			post(ProjectController.projectBaseURI + "/projects")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(projectRequest))
			)
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", ProjectController.projectBaseURI + "/projects" + "/1"))
			.andReturn();
		
		ProjectResponse actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<ProjectResponse>() { });
		ProjectResponse expectedResponse = new ProjectResponse(project);
		
		// Assert
		verify(projectRepositoryMock, atLeast(1)).save(projectNoId);
		assertEquals("Invalid project response", expectedResponse, actualResponse);
	}
	
	@Test
	@DisplayName("Get project")
	void getProject() throws Exception {
		// Arrange
		Optional<Project> projectOptional = Optional.of(project);
		
		when(projectRepositoryMock.findById(1)).thenReturn(projectOptional);
		
		// Act
		MvcResult result = this.mockMvc.perform(
				get(ProjectController.projectBaseURI + "/projects" + "/1")
			)
			.andExpect(status().is2xxSuccessful())
			.andReturn();
		
		ProjectResponse expectedResponse = new ProjectResponse(project);
		ProjectResponse actualResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<ProjectResponse>() { });
		
		// Assert
		verify(projectRepositoryMock, times(1)).findById(1);
		assertEquals("Invalid project response", expectedResponse, actualResponse);
	}
	
	@Test
	@DisplayName("Update project")
	void updateProject() throws Exception {
		// Arrange
		Project projectUpdated = new Project()
			.setId(1)
			.setName("NAME")
			.setDescription("Description")
			.setWebsite("server.com");
//			.setTeamId(1);
		
		Optional<Project> projectOptional = Optional.of(project);
		
		when(projectRepositoryMock.findById(1)).thenReturn(projectOptional);
		when(projectRepositoryMock.save(any(Project.class))).thenReturn(projectUpdated);
		
		ProjectRequest projectRequest = new ProjectRequest()
			.setName("NAME");
		
		// Act
		MvcResult result = this.mockMvc.perform(
			patch(ProjectController.projectBaseURI + "/projects" + "/1")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(projectRequest))
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id").exists())
			.andReturn();
		
		ProjectResponse expectedResponse = new ProjectResponse(project);
		expectedResponse.setName("NAME");
		ProjectResponse actualResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<ProjectResponse>() { });
		
		// Assert
		verify(projectRepositoryMock, times(1)).findById(1);
		verify(projectRepositoryMock, times(1)).save(projectUpdated);
		assertEquals("Invalid project response", expectedResponse, actualResponse);
	}
	
	@Test
	@DisplayName("Delete project")
	void deleteProject() throws Exception {
		// Act
		this.mockMvc.perform(
				delete(ProjectController.projectBaseURI + "/projects" + "/1")
			)
			.andExpect(status().isNoContent());
		
		// Assert
		verify(projectRepositoryMock, times(1)).deleteById(1);
	}
	
}