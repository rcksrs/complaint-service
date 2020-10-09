package com.rcksrs.complaintservice.domain.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.rcksrs.complaintservice.domain.Contact;
import com.rcksrs.complaintservice.domain.User;

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

	@NotBlank(message = "Fill in the id field")
	private String id;
	private String name;
	private String country;
	private String state;
	private String city;
	private List<Contact> contacts;
	
	public static UserDTO fromUser(User user) {
		return UserDTO.builder()
				.id(user.getId())
				.name(user.getName())
				.country(user.getAddress().getCountry())
				.state(user.getAddress().getState())
				.city(user.getAddress().getCity())
				.contacts(user.getContacts())
				.build();
	}

}
