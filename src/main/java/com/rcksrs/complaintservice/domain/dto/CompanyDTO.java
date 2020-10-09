package com.rcksrs.complaintservice.domain.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.rcksrs.complaintservice.domain.Contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO implements Serializable {
	private static final long serialVersionUID = -6248065233686505394L;
	
	@NotBlank(message = "Fill in the id field")
	private String id;
	private String name;
	private String cnpj;
	private String country;
	private String state;
	private String city;
	private List<Contact> contacts;

}
