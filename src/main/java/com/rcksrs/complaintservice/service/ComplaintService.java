package com.rcksrs.complaintservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rcksrs.complaintservice.domain.Complaint;
import com.rcksrs.complaintservice.domain.Reply;
import com.rcksrs.complaintservice.domain.dto.UserDTO;
import com.rcksrs.complaintservice.exception.BusinessException;
import com.rcksrs.complaintservice.exception.DuplicatedResourceException;
import com.rcksrs.complaintservice.exception.ResourceNotFoundException;
import com.rcksrs.complaintservice.repository.ComplaintRepository;
import com.rcksrs.complaintservice.repository.UserRepository;
import com.rcksrs.complaintservice.service.client.CompanyService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ComplaintService {
	
	private ComplaintRepository complaintRepository;
	private UserRepository userRepository;
	private CompanyService companyService;
	
	public Page<Complaint> findAllByTitle(String title, Pageable pageable) {
		return complaintRepository.findByTitleContainingIgnoreCase(title, pageable);
	}
	
	public Page<Complaint> findAllByDescription(String description, Pageable pageable) {
		return complaintRepository.findByDescriptionContainingIgnoreCase(description, pageable);
	}
	
	public Page<Complaint> findAllByUserId(String userId, Pageable pageable) {
		return complaintRepository.findByUserId(userId, pageable);
	}
	
	public Page<Complaint> findAllByUserStateAndUserCity(String state, String city, Pageable pageable) {
		return complaintRepository.findByUserStateAndUserCity(state, city, pageable);
	}
	
	public Page<Complaint> findAllByCompanyId(String companyId, Pageable pageable) {
		return complaintRepository.findByCompanyId(companyId, pageable);
	}
	
	public Page<Complaint> findAllByCompanyStateAndCity(String state, String city, Pageable pageable) {
		return complaintRepository.findByCompanyStateAndCompanyCity(state, city, pageable);
	}
	
	public Complaint findById(String id) {
		return complaintRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public Complaint save(Complaint complaint) {		
		if(complaint.getId() == null) {
			var user = userRepository.findById(complaint.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("User was not found"));
			var company = companyService.findCompanyById(complaint.getCompany().getId());
			if(company == null) throw new ResourceNotFoundException("Company was not found");
			
			complaint.setDate(LocalDate.now());
			complaint.setIsActive(true);
			complaint.setReplies(new ArrayList<>());
			complaint.setUser(UserDTO.fromUser(user));
			complaint.setCompany(company);
			
			return complaintRepository.save(complaint);
		}
		throw new DuplicatedResourceException();
	}
	
	public Complaint reply(Reply reply) {
		var complaint = complaintRepository.findById(reply.getComplaintId()).orElseThrow(() -> new ResourceNotFoundException());
		reply.setDate(LocalDateTime.now());
		complaint.getReplies().add(reply);
		return complaintRepository.save(complaint);
	}
	
	public Complaint close(Complaint complaint) {
		if(complaint.getRating() == null) throw new BusinessException("Fill in the rating field");
		var complaintSaved = complaintRepository.findById(complaint.getId()).orElseThrow(() -> new ResourceNotFoundException());
		complaint.setIsActive(false);
		return complaintRepository.save(complaintSaved);
	}
	
	public void delete(Complaint complaint) {
		var complaintSaved = complaintRepository.findById(complaint.getId()).orElseThrow(() -> new ResourceNotFoundException());
		complaintRepository.deleteById(complaintSaved.getId());
	}

}
