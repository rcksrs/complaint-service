package com.rcksrs.complaintservice.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
	private static final long serialVersionUID = -2334526699098318527L;

	@Id
	private String id;
	
	@NotBlank(message = "Fill in the name field")
	private String name;
	
	@NotBlank(message = "Fill in the CPF field")
	@CPF(message = "Invalid CPF")
	@Indexed
	private String cpf;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING) 
	private LocalDate birthdate;
	
	@NotNull(message = "Fill in the address field")
	private Address address;
	
	@NotNull(message = "Fill in at least one contact field")
	private List<Contact> contacts;

}
