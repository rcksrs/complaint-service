package com.rcksrs.complaintservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.rcksrs.complaintservice.domain.Complaint;

public interface ComplaintRepository extends MongoRepository<Complaint, String> {
	Page<Complaint> findByTitleContainingIgnoreCase(String title, Pageable pageable);
	Page<Complaint> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
//	Page<Complaint> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);
	Page<Complaint> findByUserId(String userId, Pageable pageable);
	Page<Complaint> findByUserStateAndUserCity(String state, String city, Pageable pageable);
	Page<Complaint> findByCompanyId(String companyId, Pageable pageable);
	Page<Complaint> findByCompanyStateAndCompanyCity(String state, String city, Pageable pageable);

}
