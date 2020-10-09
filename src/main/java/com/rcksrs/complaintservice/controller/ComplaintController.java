package com.rcksrs.complaintservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rcksrs.complaintservice.domain.Complaint;
import com.rcksrs.complaintservice.domain.Reply;
import com.rcksrs.complaintservice.service.ComplaintService;

@RestController
@RequestMapping("/api/v1")
public class ComplaintController {
	
	@Autowired
	private ComplaintService complaintService;
	
	@GetMapping("/filter/title/{title}")
	public ResponseEntity<Page<Complaint>> findAllByTitle(@PathVariable String title, @PageableDefault(sort = "name", size = 20) Pageable pageable) {
		var complaints = complaintService.findAllByTitle(title, pageable);
		return ResponseEntity.ok(complaints); 
	}
	
	@GetMapping("/filter/description/{description}")
	public ResponseEntity<Page<Complaint>> findAllByDescription(@PathVariable String description, @PageableDefault(sort = "name", size = 20) Pageable pageable) {
		var complaints = complaintService.findAllByDescription(description, pageable);
		return ResponseEntity.ok(complaints);
	}
	
	@GetMapping("/filter/user/{userId}")
	public ResponseEntity<Page<Complaint>> findAllByUserId(@PathVariable String userId, @PageableDefault(sort = "name", size = 20) Pageable pageable) {
		var complaints = complaintService.findAllByUserId(userId, pageable);
		return ResponseEntity.ok(complaints);
	}
	
	@GetMapping("/filter/user/locale")
	public ResponseEntity<Page<Complaint>> findAllByUserStateAndUserCity(@RequestParam String state, @RequestParam String city, @PageableDefault(sort = "name", size = 20) Pageable pageable) {
		var complaints = complaintService.findAllByUserStateAndUserCity(state, city, pageable);
		return ResponseEntity.ok(complaints);
	}
	
	@GetMapping("/filter/company/{companyId}")
	public ResponseEntity<Page<Complaint>> findAllByCompanyId(@PathVariable String companyId, @PageableDefault(sort = "name", size = 20) Pageable pageable) {
		var complaints = complaintService.findAllByCompanyId(companyId, pageable);
		return ResponseEntity.ok(complaints);
	}
	
	@GetMapping("/filter/company/locale")
	public ResponseEntity<Page<Complaint>> findAllByCompanyStateAndCity(@RequestParam String state, @RequestParam String city, @PageableDefault(sort = "name", size = 20) Pageable pageable) {
		var complaints = complaintService.findAllByCompanyStateAndCity(state, city, pageable);
		return ResponseEntity.ok(complaints);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Complaint> findById(@PathVariable String id) {
		var complaint = complaintService.findById(id);
		return ResponseEntity.ok(complaint);
	}
	
	@PostMapping
	public ResponseEntity<Complaint> save(@RequestBody @Valid Complaint complaint) {
		var complaintSaved = complaintService.save(complaint);
		var location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(complaintSaved.getId()).toUri();
		return ResponseEntity.created(location).body(complaintSaved);
	}
	
	@PostMapping("/reply")
	public ResponseEntity<Complaint> reply(@RequestBody @Valid Reply reply, @PathVariable String complaintId) {
		var complaint = complaintService.reply(reply, complaintId);
		return ResponseEntity.ok(complaint);
	}
	
	@PostMapping("/close")
	public ResponseEntity<Complaint> close(@RequestBody @Valid Complaint complaint) {
		var complaintSaved = complaintService.close(complaint);
		return ResponseEntity.ok(complaintSaved);
	}
	
	@DeleteMapping
	public ResponseEntity<Complaint> delete(@RequestBody @Valid Complaint complaint) {
		complaintService.delete(complaint);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
