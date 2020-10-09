package com.rcksrs.complaintservice.domain;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	private String zipCode;
	
	@NotBlank(message = "Fill in the country field")
	private String country;
	
	@NotBlank(message = "Fill in the state field")
	private String state;
	
	@NotBlank(message = "Fill in the city field")
	private String city;
	
	private String street;

}
