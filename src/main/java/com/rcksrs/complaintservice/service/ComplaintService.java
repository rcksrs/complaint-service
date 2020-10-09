package com.rcksrs.complaintservice.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rcksrs.complaintservice.domain.Complaint;
import com.rcksrs.complaintservice.exception.DuplicatedResourceException;
import com.rcksrs.complaintservice.exception.ResourceNotFoundException;
import com.rcksrs.complaintservice.repository.ComplaintRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ComplaintService {
	
	private ComplaintRepository complaintRepository;
	
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
	
	public Complaint save(Complaint complaint) {
		if(complaint.getId() == null) {
			complaint.setDate(LocalDate.now());
			return complaintRepository.save(complaint);
		}
		throw new DuplicatedResourceException();
	}
	
	public Complaint update(Complaint complaint) {
		if(complaint.getId() != null) {
			return complaintRepository.save(complaint);
		}
		throw new ResourceNotFoundException();
	}
	
	public void delete(Complaint complaint) {
		var complaintSaved = complaintRepository.findById(complaint.getId()).orElseThrow(() -> new ResourceNotFoundException());
		complaintRepository.deleteById(complaintSaved.getId());
	}

}
