package com.rcksrs.complaintservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rcksrs.complaintservice.domain.ContactType;
import com.rcksrs.complaintservice.domain.User;
import com.rcksrs.complaintservice.exception.BusinessException;
import com.rcksrs.complaintservice.exception.DuplicatedResourceException;
import com.rcksrs.complaintservice.exception.ResourceNotFoundException;
import com.rcksrs.complaintservice.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
	private UserRepository userRepository;
	
	public User findById(String id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public User findByCpf(String cpf) {
		return userRepository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException());
	}	
	
	public Page<User> findAllByName(String name, Pageable pageable) {
		return userRepository.findByNameContainingIgnoreCase(name, pageable);		
	}
	
	public Page<User> findAllByExactName(String name, Pageable pageable) {
		return userRepository.findByNameIgnoreCase(name, pageable);
	}
	
	public User save(User user) {
		if(user.getId() != null || userRepository.findByCpf(user.getCpf()).isPresent()) throw new DuplicatedResourceException();
		if(user.getContacts().stream().filter(c -> c.getType() == ContactType.PHONE).count() > 0) throw new BusinessException("Fill in at least one phone number");
		if(user.getContacts().stream().filter(c -> c.getType() == ContactType.EMAIL).count() > 0) throw new BusinessException("Fill in at least one email");
		
		return userRepository.save(user);
	}
	
	public User update(User user) {
		if(user.getContacts().stream().filter(c -> c.getType() == ContactType.PHONE).count() > 0) throw new BusinessException("Fill in at least one phone number");
		if(user.getContacts().stream().filter(c -> c.getType() == ContactType.EMAIL).count() > 0) throw new BusinessException("Fill in at least one email");		
		userRepository.findByIdAndCpf(user.getId(), user.getCpf()).orElseThrow(() -> new BusinessException("User's CPF cannot be changed"));
		
		return userRepository.save(user);
	}
	
	public void delete(User user) {
		var userSaved = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException());
		userRepository.deleteById(userSaved.getId());
	}

}
