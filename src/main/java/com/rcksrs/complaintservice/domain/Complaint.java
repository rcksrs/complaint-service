package com.rcksrs.complaintservice.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.rcksrs.complaintservice.domain.dto.CompanyDTO;
import com.rcksrs.complaintservice.domain.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Complaint implements Serializable {
	private static final long serialVersionUID = -5913813488761289154L;
	
	@Id
	private String id;
	
	@NotBlank(message = "Fill in the title field")
	private String title;
	
	private LocalDate date;
	
	@NotBlank(message = "Fill in the description field")
	@Size(min = 50, message = "Complaint should have at least 50 characters")
	private String description;
	
	@NotNull(message = "Fill in the user field")
	private UserDTO user;
	
	@NotNull(message = "Fill in the company field")
	private CompanyDTO company;
	
	private List<Reply> replies;
	
	private Boolean isActive;
	
	@Min(value = 0, message = "Rating must be greater than or equal to 0")
	@Max(value = 10, message = "Rating must be less than or equal to 10")
	private Integer rating;

}
