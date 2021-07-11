package com.waracle.cakemgr.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModelFieldTest {

	private ModelField field;

	@BeforeEach
	void setUp() throws Exception {
		Method method = ExampleTestClass.class.getMethod("getName");
		field = new ModelField(ExampleTestClass.class, method);
	}

	@Test
	void jsonName() throws NoSuchMethodException, SecurityException {
		assertEquals("name", field.getJsonName());
	}

	@Test
	void readField() throws Exception {
		ExampleTestClass object = new ExampleTestClass();
		object.setName("Fred");
		
		String data = (String) field.getEntryData(object);
		assertEquals("Fred", data);
	}
	
	
}
