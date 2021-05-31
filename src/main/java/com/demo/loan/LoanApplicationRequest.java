package com.demo.loan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Representation of Application Request")
public class LoanApplicationRequest  {
	@ApiModelProperty(hidden = true)
	private String applicationId;
	@ApiModelProperty(value = "National Identification number", required = true)
    private String ssn;
	@ApiModelProperty(value = "First Name of applicant", required = true)
    private String name;
	@ApiModelProperty(value = "Last name of applicant", required = true)
    private String lastName;
	@ApiModelProperty(value = "E-mail of applicant", required = true)
    private String email;
	@ApiModelProperty(value = "Phone Number of applicant", required = true)
    private String phoneNumber;
	@ApiModelProperty(value = "applicant employment status", required = true)
    private EmploymentStatus employmentStatus;
    
}