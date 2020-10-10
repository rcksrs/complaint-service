package com.rcksrs.complaintservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

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
import com.rcksrs.complaintservice.domain.Address;
import com.rcksrs.complaintservice.domain.Contact;
import com.rcksrs.complaintservice.domain.ContactType;
import com.rcksrs.complaintservice.domain.User;
import com.rcksrs.complaintservice.exception.BusinessException;
import com.rcksrs.complaintservice.exception.ResourceNotFoundException;
import com.rcksrs.complaintservice.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	private static ObjectMapper mapper;
	
	private static User user;
	
	@BeforeAll
	static void beforeAll() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		user = User.builder()
				.id("1")
				.name("Nelson Yuri")
				.cpf("39911697207")
				.birthdate(LocalDate.of(1990, 01, 01))
				.address(new Address("02319160", "Brazil", "Maranhao", "Sao Luis", "Rua Cargo 977"))
				.contacts(List.of(new Contact("98997849063", ContactType.PHONE), new Contact("nelson@email.com", ContactType.EMAIL)))
				.build();
	}
	
	@Test
	@DisplayName("Should return status code 200 when find a user by id")
	void testFindById() throws Exception {
		when(userService.findById("1")).thenReturn(user);
		
		mockMvc.perform(get("/api/v1/user/1")
				.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(user)));
		
	}

	@Test
	@DisplayName("Should return status code 201 when a valid user is saved")
	void testSave() throws Exception {
		when(userService.save(any(User.class))).thenReturn(user);
		
		mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(user)))
                .andExpect(header().string("Location", "http://localhost/api/v1/user/1"));
	}

	@Test
	@DisplayName("Should return status code 200 when a valid user is updated")
	void testUpdate() throws Exception {
		when(userService.update(any(User.class))).thenReturn(user);
		
		mockMvc.perform(put("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(user)));
	}

	@Test
	@DisplayName("Should return status code 200 when a valid user is deleted")
	void testDelete() throws Exception {
		doNothing().when(userService).delete(any(User.class));
		
        mockMvc.perform(delete("/api/v1/user")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Should return status code 404 when not find a user by id")
	void testNotFindById() throws Exception {
		when(userService.findById(any(String.class))).thenThrow(ResourceNotFoundException.class);
		
		mockMvc.perform(get("/api/v1/user/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Should return status code 400 when a invalid user is provided")
	void testNotSave() throws Exception {
		mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User())))
                .andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Should return status code 400 when a user without email is provided")
	void testNotSaveWithoutEmail() throws Exception {
		when(userService.save(any(User.class))).thenThrow(BusinessException.class);
		
		mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Should return status code 400 when a invalid user is updated")
	void testNotUpdate() throws Exception {
		mockMvc.perform(put("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User())))
                .andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Should return status code 400 when try to delete a invalid user")
	void testNotDelete() throws Exception {
		mockMvc.perform(delete("/api/v1/user")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsString(new User())))
                .andExpect(status().isBadRequest());
	}

}
