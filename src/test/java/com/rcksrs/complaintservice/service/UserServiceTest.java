package com.rcksrs.complaintservice.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.rcksrs.complaintservice.domain.Address;
import com.rcksrs.complaintservice.domain.Contact;
import com.rcksrs.complaintservice.domain.ContactType;
import com.rcksrs.complaintservice.domain.User;
import com.rcksrs.complaintservice.exception.BusinessException;
import com.rcksrs.complaintservice.exception.DuplicatedResourceException;
import com.rcksrs.complaintservice.exception.ResourceNotFoundException;
import com.rcksrs.complaintservice.repository.UserRepository;

@SpringBootTest
class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserService userService;
	
	private static User user;
	
	@BeforeAll
	static void beforeAll() {
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
	@DisplayName("Should find a user when a user id is provided")
	void testFindById() {
		when(userRepository.findById("1")).thenReturn(Optional.of(user));
		
		var userSaved = userService.findById("1");
		
		assertAll(() -> {
			assertEquals("Nelson Yuri", userSaved.getName());
			assertEquals("39911697207", userSaved.getCpf());
			assertEquals(2, userSaved.getContacts().size());
			assertEquals("02319160", userSaved.getAddress().getZipCode());
		});
	}

	@Test
	@DisplayName("Should save a user")
	void testSave() {
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(userRepository.findByCpf("39911697207")).thenReturn(Optional.empty());
		
		var newUser = User.builder()
				.name("Nelson Yuri")
				.cpf("39911697207")
				.birthdate(LocalDate.of(1990, 01, 01))
				.address(new Address("02319160", "Brazil", "Maranhao", "Sao Luis", "Rua Cargo 977"))
				.contacts(List.of(new Contact("98997849063", ContactType.PHONE), new Contact("nelson@email.com", ContactType.EMAIL)))
				.build();
		
		var userSaved = userService.save(newUser);
		
		assertAll(() -> {
			assertEquals("Nelson Yuri", userSaved.getName());
			assertEquals("39911697207", userSaved.getCpf());
			assertEquals(2, userSaved.getContacts().size());
			assertEquals("02319160", userSaved.getAddress().getZipCode());
		});		
	}

	@Test
	@DisplayName("Should update a user")
	void testUpdate() {
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(userRepository.findByIdAndCpf("1", "39911697207")).thenReturn(Optional.of(user));
		
		var userSaved = userService.update(user);
		
		assertAll(() -> {
			assertEquals("Nelson Yuri", userSaved.getName());
			assertEquals("39911697207", userSaved.getCpf());
			assertEquals(2, userSaved.getContacts().size());
			assertEquals("02319160", userSaved.getAddress().getZipCode());
		});
	}

	@Test
	@DisplayName("Should delete a user")
	void testDelete() {
		when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));
		doNothing().when(userRepository).deleteById("1");
		
		assertDoesNotThrow(() -> userService.delete(user));
	}
	
	@Test
	@DisplayName("Should not delete a user")
	void testNotDelete() {
		when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> userService.delete(user));
	}
	
	@Test
	@DisplayName("Should not save a user when CPF already exists")
	void testNotSaveCpfExists() {
		when(userRepository.findByCpf("39911697207")).thenReturn(Optional.of(user));
		
		var newUser = User.builder()
				.name("Nelson Yuri")
				.cpf("39911697207")
				.birthdate(LocalDate.of(1990, 01, 01))
				.address(new Address("02319160", "Brazil", "Maranhao", "Sao Luis", "Rua Cargo 977"))
				.contacts(List.of(new Contact("98997849063", ContactType.PHONE), new Contact("nelson@email.com", ContactType.EMAIL)))
				.build();
		
		assertThrows(DuplicatedResourceException.class, () -> userService.save(newUser));	
	}
	
	@Test
	@DisplayName("Should not save a user when email is missing")
	void testNotSaveWithoutEmail() {
		when(userRepository.findByCpf("39911697207")).thenReturn(Optional.empty());
		
		var newUser = User.builder()
				.name("Nelson Yuri")
				.cpf("39911697207")
				.birthdate(LocalDate.of(1990, 01, 01))
				.address(new Address("02319160", "Brazil", "Maranhao", "Sao Luis", "Rua Cargo 977"))
				.contacts(List.of(new Contact("98997849063", ContactType.PHONE)))
				.build();
		
		assertThrows(BusinessException.class, () -> userService.save(newUser));	
	}
	
	@Test
	@DisplayName("Should not save a user when phone number is missing")
	void testNotSaveWithoutPhone() {
		when(userRepository.findByCpf("39911697207")).thenReturn(Optional.empty());
		
		var newUser = User.builder()
				.name("Nelson Yuri")
				.cpf("39911697207")
				.birthdate(LocalDate.of(1990, 01, 01))
				.address(new Address("02319160", "Brazil", "Maranhao", "Sao Luis", "Rua Cargo 977"))
				.contacts(List.of(new Contact("nelson@email.com", ContactType.EMAIL)))
				.build();
		
		assertThrows(BusinessException.class, () -> userService.save(newUser));	
	}
	
	@Test
	@DisplayName("Should not update a user when CPF changed")
	void testNotUpdateCpfChanged() {
		when(userRepository.findByIdAndCpf("1", "39911697207")).thenReturn(Optional.empty());
		
		var newUser = User.builder()
				.id("1")
				.name("Nelson Yuri")
				.cpf("39911697207")
				.birthdate(LocalDate.of(1990, 01, 01))
				.address(new Address("02319160", "Brazil", "Maranhao", "Sao Luis", "Rua Cargo 977"))
				.contacts(List.of(new Contact("98997849063", ContactType.PHONE), new Contact("nelson@email.com", ContactType.EMAIL)))
				.build();
		
		assertThrows(BusinessException.class, () -> userService.update(newUser));	
	}
	
	@Test
	@DisplayName("Should not update a user when email is missing")
	void testNotUpdateWithoutEmail() {
		when(userRepository.findByCpf("39911697207")).thenReturn(Optional.empty());
		
		var newUser = User.builder()
				.name("Nelson Yuri")
				.cpf("39911697207")
				.birthdate(LocalDate.of(1990, 01, 01))
				.address(new Address("02319160", "Brazil", "Maranhao", "Sao Luis", "Rua Cargo 977"))
				.contacts(List.of(new Contact("98997849063", ContactType.PHONE)))
				.build();
		
		assertThrows(BusinessException.class, () -> userService.update(newUser));	
	}
	
	@Test
	@DisplayName("Should not update a user when phone number is missing")
	void testNotUpdateWithoutPhone() {
		when(userRepository.findByCpf("39911697207")).thenReturn(Optional.empty());
		
		var newUser = User.builder()
				.name("Nelson Yuri")
				.cpf("39911697207")
				.birthdate(LocalDate.of(1990, 01, 01))
				.address(new Address("02319160", "Brazil", "Maranhao", "Sao Luis", "Rua Cargo 977"))
				.contacts(List.of(new Contact("nelson@email.com", ContactType.EMAIL)))
				.build();
		
		assertThrows(BusinessException.class, () -> userService.update(newUser));	
	}
	
	@Test
	@DisplayName("Should not find a user")
	void testNotFindById() {
		when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> userService.findById("1"));
	}

}
