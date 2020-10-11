package com.rcksrs.complaintservice.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.rcksrs.complaintservice.domain.Address;
import com.rcksrs.complaintservice.domain.Complaint;
import com.rcksrs.complaintservice.domain.Contact;
import com.rcksrs.complaintservice.domain.ContactType;
import com.rcksrs.complaintservice.domain.Reply;
import com.rcksrs.complaintservice.domain.User;
import com.rcksrs.complaintservice.domain.dto.CompanyDTO;
import com.rcksrs.complaintservice.domain.dto.UserDTO;
import com.rcksrs.complaintservice.exception.BusinessException;
import com.rcksrs.complaintservice.exception.DuplicatedResourceException;
import com.rcksrs.complaintservice.exception.ResourceNotFoundException;
import com.rcksrs.complaintservice.repository.ComplaintRepository;
import com.rcksrs.complaintservice.repository.UserRepository;
import com.rcksrs.complaintservice.service.client.CompanyService;

@SpringBootTest
class ComplaintServiceTest {
	
	@Mock
	private ComplaintRepository complaintRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private CompanyService companyService;
	
	@InjectMocks
	private ComplaintService complaintService;
	
	private static Complaint complaint;
	private static User user;
	private static CompanyDTO company;
	
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
		
		company = CompanyDTO.builder()
				.id("1")
				.cnpj("23647496000195")
				.name("Lorem ipsum dolor")
				.country("Brazil")
				.state("Maranhao")
				.city("Sao Luis")
				.contacts(List.of(new Contact("9827849063", ContactType.PHONE)))
				.build();
	}

	@Test
	@DisplayName("Should save a complaint")
	void testSave() {
		when(userRepository.findById("1")).thenReturn(Optional.of(user));
		when(companyService.findCompanyById("1")).thenReturn(company);
		when(complaintRepository.save(any(Complaint.class))).thenReturn(complaint);
		
		var newComplaint = Complaint.builder()
				.title("Aenean vel tortor")
				.description("Sed consectetur dui quis est commodo, ut tincidunt sapien fringilla")
				.user(UserDTO.builder().id("1").build())
				.company(CompanyDTO.builder().id("1").build())
				.build();
		
		var complaintSaved = complaintService.save(newComplaint);
		assertAll(() -> {
			assertEquals("Aenean vel tortor", complaintSaved.getTitle());
			assertEquals("1", complaintSaved.getUser().getId());
			assertEquals("1", complaintSaved.getCompany().getId());
		});		
	}
	
	@Test
	@DisplayName("Should not save a complaint when user was not found")
	void testNotSaveUserNotFound() {
		when(userRepository.findById("1")).thenReturn(Optional.empty());
		when(companyService.findCompanyById("1")).thenReturn(company);
		
		var newComplaint = Complaint.builder()
				.title("Aenean vel tortor")
				.description("Sed consectetur dui quis est commodo, ut tincidunt sapien fringilla")
				.user(UserDTO.builder().id("1").build())
				.company(CompanyDTO.builder().id("1").build())
				.build();
		
		assertThrows(ResourceNotFoundException.class, () -> complaintService.save(newComplaint));		
	}
	
	@Test
	@DisplayName("Should not save a complaint when company was not found")
	void testNotSaveCompanyNotFound() {
		when(userRepository.findById("1")).thenReturn(Optional.empty());
		when(companyService.findCompanyById("1")).thenReturn(null);
		
		var newComplaint = Complaint.builder()
				.title("Aenean vel tortor")
				.description("Sed consectetur dui quis est commodo, ut tincidunt sapien fringilla")
				.user(UserDTO.builder().id("1").build())
				.company(CompanyDTO.builder().id("1").build())
				.build();
		
		assertThrows(ResourceNotFoundException.class, () -> complaintService.save(newComplaint));
		
	}
	
	@Test
	@DisplayName("Should not save a complaint when complaint already exists")
	void testNotSaveDuplicated() {		
		assertThrows(DuplicatedResourceException.class, () -> complaintService.save(complaint));		
	}

	@Test
	@DisplayName("Should reply a complaint")
	void testReply() {
		when(complaintRepository.findById("1")).thenReturn(Optional.of(complaint));
		when(complaintRepository.save(any(Complaint.class))).thenReturn(complaint);
		
		var reply = Reply.builder()
				.complaintId("1")
				.author("Nelson Yuri")
				.description("Lorem ipsum dolor sit amet, consectetur adipiscing elit")
				.build();
		
		var complaintSaved = complaintService.reply(reply);
		assertAll(() -> {
			assertEquals("Aenean vel tortor", complaintSaved.getTitle());
			assertEquals("1", complaintSaved.getUser().getId());
			assertEquals("1", complaintSaved.getCompany().getId());
			assertEquals(1, complaintSaved.getReplies().size());
		});		
	}
	
	@Test
	@DisplayName("Should not reply a complaint when complaint was not found")
	void testNotReply() {
		when(complaintRepository.findById("1")).thenReturn(Optional.empty());
		
		var reply = Reply.builder()
				.complaintId("1")
				.author("Nelson Yuri")
				.description("Lorem ipsum dolor sit amet, consectetur adipiscing elit")
				.build();
		
		assertThrows(ResourceNotFoundException.class, () -> complaintService.reply(reply));		
	}

	@Test
	@DisplayName("Should close a complaint")
	void testClose() {
		when(complaintRepository.findById("1")).thenReturn(Optional.of(complaint));
		when(complaintRepository.save(any(Complaint.class))).thenReturn(complaint);
		
		var complaintSaved = complaintService.close(complaint);
		assertAll(() -> {
			assertEquals("Aenean vel tortor", complaintSaved.getTitle());
			assertEquals(10, complaintSaved.getRating());
			assertFalse(complaintSaved.getIsActive());
		});		
	}
	
	@Test
	@DisplayName("Should not close a complaint when rating is missing")
	void testNotClose() {
		complaint.setRating(null);
		assertThrows(BusinessException.class, () -> complaintService.close(complaint));
		complaint.setRating(10);		
	}
	
	@Test
	@DisplayName("Should not close a complaint when complaint was not found")
	void testNotCloseComplaintNotFound() {
		when(complaintRepository.findById("1")).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> complaintService.close(complaint));		
	}

	@Test
	@DisplayName("Should delete a complaint")
	void testDelete() {
		when(complaintRepository.findById(any(String.class))).thenReturn(Optional.of(complaint));
		doNothing().when(complaintRepository).deleteById("1");
		
		assertDoesNotThrow(() -> complaintService.delete(complaint));
	}
	
	@Test
	@DisplayName("Should not delete a complaint")
	void testNotDelete() {
		when(complaintRepository.findById(any(String.class))).thenReturn(Optional.empty());		
		assertThrows(ResourceNotFoundException.class, () -> complaintService.delete(complaint));
	}

}
