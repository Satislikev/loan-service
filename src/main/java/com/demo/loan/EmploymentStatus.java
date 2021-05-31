package com.demo.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EmploymentStatus {
	FULLTIME("FULLTIME"), PARTTIME("PARTTIME"), UNEMPLOYED("UNEMPLOYED");
	
	@Getter private String value;
}
