package com.rcksrs.complaintservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rcksrs.complaintservice.domain.dto.CompanyDTO;

@FeignClient("company-service")
public interface CompanyService {
	
	@RequestMapping("/api/v1/find/{id}")
	CompanyDTO findCompanyById(@PathVariable String id);

}
