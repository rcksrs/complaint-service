package com.rcksrs.complaintservice.domain;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact implements Serializable {
	private static final long serialVersionUID = 5770121932947975255L;

	@NotBlank(message = "Fill in the contact field")
	private String contact;
	
	@NotNull(message = "Fill in the contact type field")
	private ContactType type;

}
