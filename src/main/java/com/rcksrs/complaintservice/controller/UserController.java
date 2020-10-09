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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rcksrs.complaintservice.domain.User;
import com.rcksrs.complaintservice.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/cpf/{cpf}")
	public ResponseEntity<User> findByCpf(@PathVariable String cpf) {
		var user = userService.findByCpf(cpf);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable String id) {
		var user = userService.findById(id);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/filter/name/{name}")
	public ResponseEntity<Page<User>> findAllByName(@PathVariable String name, @PageableDefault(sort = "name", size = 20) Pageable pageable) {
		var users = userService.findAllByName(name, pageable);
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Page<User>> findAllByExactName(@PathVariable String name, @PageableDefault(sort = "name", size = 20) Pageable pageable) {
		var users = userService.findAllByExactName(name, pageable);
		return ResponseEntity.ok(users);
	}
	
	@PostMapping
	public ResponseEntity<User> save(@RequestBody @Valid User user) {
		var userSaved = userService.save(user);
		var location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(userSaved.getId()).toUri();
		return ResponseEntity.created(location).body(userSaved);
	}
	
	@PutMapping
	public ResponseEntity<User> update(@RequestBody @Valid User user) {
		var userSaved = userService.update(user);
		return ResponseEntity.ok(userSaved);
	}
	
	@DeleteMapping
	public ResponseEntity<User> delete(@RequestBody @Valid User user) {
		userService.delete(user);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
