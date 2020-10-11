package com.rcksrs.complaintservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rcksrs.complaintservice.domain.Complaint;
import com.rcksrs.complaintservice.domain.Reply;
import com.rcksrs.complaintservice.domain.dto.CompanyDTO;
import com.rcksrs.complaintservice.domain.dto.UserDTO;
import com.rcksrs.complaintservice.exception.ResourceNotFoundException;
import com.rcksrs.complaintservice.service.ComplaintService;

@WebMvcTest(ComplaintController.class)
class ComplaintControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ComplaintService complaintService;
	
	private static ObjectMapper mapper;
	
	private static Complaint complaint;
	
	@BeforeAll
	static void beforeAll() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		complaint = Complaint.builder()
				.id("1")
				.title("Aenean vel tortor")
				.description("Sed consectetur dui quis est commodo, ut tincidunt sapien fringilla")
				.user(UserDTO.builder().id("1").build())
				.company(CompanyDTO.builder().id("1").build())
				.replies(new ArrayList<>())
				.rating(10)
				.isActive(true)
				.build();
	}
	
	@Test
	@DisplayName("Should return status code 200 when find a complaint by id")
	void testFindById() throws Exception {
		when(complaintService.findById("1")).thenReturn(complaint);
		
		mockMvc.perform(get("/api/v1/complaint/1")
				.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(complaint)));		
	}
	
	@Test
	@DisplayName("Should return status code 404 when not find a complaint by id")
	void testNotFindById() throws Exception {
		when(complaintService.findById(any(String.class))).thenThrow(ResourceNotFoundException.class);
		
		mockMvc.perform(get("/api/v1/complaint/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Should return status code 201 when a valid complaint is saved")
	void testSave() throws Exception {
		when(complaintService.save(any(Complaint.class))).thenReturn(complaint);
		
		mockMvc.perform(post("/api/v1/complaint")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(complaint)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(complaint)))
                .andExpect(header().string("Location", "http://localhost/api/v1/complaint/1"));
	}
	
	@Test
	@DisplayName("Should return status code 400 when a invalid complaint is provided")
	void testNotSave() throws Exception {		
		mockMvc.perform(post("/api/v1/complaint")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Complaint())))
                .andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Should return status code 200 when a valid reply is saved")
	void testReply() throws Exception {
		when(complaintService.reply(any(Reply.class))).thenReturn(complaint);
		
		var reply = Reply.builder()
				.complaintId("1")
				.author("Nelson Yuri")
				.description("Lorem ipsum dolor sit amet, consectetur adipiscing elit")
				.build();
		
		mockMvc.perform(post("/api/v1/complaint/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reply)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(complaint)));
	}
	
	@Test
	@DisplayName("Should return status code 400 when a invalid reply is provided")
	void testNotReply() throws Exception {
		mockMvc.perform(post("/api/v1/complaint/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Reply())))
                .andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Should return status code 200 when a valid complaint is closed")
	void testClose() throws Exception {
		when(complaintService.close(any(Complaint.class))).thenReturn(complaint);
		
		mockMvc.perform(post("/api/v1/complaint/close")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(complaint)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(complaint)));
	}
	
	@Test
	@DisplayName("Should return status code 400 when a invalid complaint is provided")
	void testNotClose() throws Exception {
		mockMvc.perform(post("/api/v1/complaint/close")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Complaint())))
                .andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Should return status code 200 when a valid complaint is deleted")
	void testDelete() throws Exception {
		doNothing().when(complaintService).delete(any(Complaint.class));
		
        mockMvc.perform(delete("/api/v1/complaint")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsString(complaint)))
                .andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Should return status code 400 when try to delete a invalid complaint")
	void testNotDelete() throws Exception {
		mockMvc.perform(delete("/api/v1/complaint")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsString(new Complaint())))
                .andExpect(status().isBadRequest());
	}

}
