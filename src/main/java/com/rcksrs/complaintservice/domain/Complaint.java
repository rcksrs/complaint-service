package com.rcksrs.complaintservice.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.rcksrs.complaintservice.domain.dto.CompanyDTO;

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
	
	private String id;
	private String title;
	private String description;
	private User user;
	private CompanyDTO company;

}
