package com.rcksrs.complaintservice.domain.dto;

import java.io.Serializable;
import java.util.List;

import com.rcksrs.complaintservice.domain.Address;
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
	
	private String id;
	private String name;
	private String cnpj;
	private Address address;
	private List<Contact> contacts;

}
