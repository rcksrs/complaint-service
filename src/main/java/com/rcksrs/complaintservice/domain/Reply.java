package com.rcksrs.complaintservice.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reply implements Serializable {
	private static final long serialVersionUID = 4391451637359480290L;
	
	@NotBlank(message = "Fill in the complaintId field")
	private String complaintId;
	
	@NotBlank(message = "Fill in the author field")
	private String author;
	
	@NotBlank(message = "Fill in the description field")
	@Size(min = 50, message = "Complaint should have at least 50 characters")
	private String description;
	
	private LocalDateTime date;

}
