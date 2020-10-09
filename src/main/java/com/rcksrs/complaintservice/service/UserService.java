package com.rcksrs.complaintservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
		var canSave = user.getId() == null && userRepository.findByCpf(user.getCpf()).isEmpty();
		if(canSave) return userRepository.save(user);
		throw new DuplicatedResourceException();
	}
	
	public User update(User user) {
		userRepository.findByIdAndCpf(user.getId(), user.getCpf()).orElseThrow(() -> new BusinessException("User's CPF cannot be changed"));
		return userRepository.save(user);
	}
	
	public void delete(User user) {
		var userSaved = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException());
		userRepository.deleteById(userSaved.getId());
	}

}
