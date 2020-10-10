package com.rcksrs.complaintservice.domain;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
	private static final long serialVersionUID = -1140633214481532102L;

	private String zipCode;
	
	@NotBlank(message = "Fill in the country field")
	private String country;
	
	@NotBlank(message = "Fill in the state field")
	private String state;
	
	@NotBlank(message = "Fill in the city field")
	private String city;
	
	private String street;

}
