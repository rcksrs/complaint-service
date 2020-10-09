package com.rcksrs.complaintservice.domain.dto;

import java.io.Serializable;
import java.util.List;

import com.rcksrs.complaintservice.domain.Contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
	private static final long serialVersionUID = -6427033964301315423L;

	private String id;
	private String name;
	private String country;
	private String state;
	private String city;
	private List<Contact> contacts;

}
