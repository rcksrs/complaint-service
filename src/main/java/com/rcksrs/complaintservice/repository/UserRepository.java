package com.rcksrs.complaintservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.rcksrs.complaintservice.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByCpf(String cpf);
	Optional<User> findByIdAndCpf(String id, String cpf);
	Page<User> findByNameIgnoreCase(String name, Pageable pageable);
	Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
