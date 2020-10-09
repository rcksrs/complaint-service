package com.rcksrs.complaintservice.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
	@NotBlank(message = "Fill in the contact field")
	private String contact;
	
	@NotNull(message = "Fill in the contact type field")
	private ContactType type;

}
