package com.waracle.cakemgr.model;

import java.time.LocalDate;

public class ExampleTestClass {

	private String name;
	private LocalDate date;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public String getNotValidField() {
		return "";
	}
}
