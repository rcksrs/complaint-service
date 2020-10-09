package com.rcksrs.complaintservice.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

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
	 
	 private String id;
	 private String name;
	 private String cpf;
	 private LocalDate birthdate;
	 private List<Contact> contacts;

}
