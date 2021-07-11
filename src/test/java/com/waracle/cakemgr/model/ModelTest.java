package com.waracle.cakemgr.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModelTest {

	@BeforeEach
	void setUp() throws Exception {
		Model.register(ExampleTestClass.class);
	}

	@Test
	void dontAddClassAgain() {
		Model model = Model.getModel(ExampleTestClass.class);
		Model newModel = Model.register(ExampleTestClass.class);
		assertEquals(model, newModel);
	}

	@Test
	void forClass() {
		Model model = Model.getModel(ExampleTestClass.class);
		assertEquals(ExampleTestClass.class, model.getForClass());
	}

	@Test
	void forClassByName() {
		Model model = Model.getModel("ExampleTestClass");
		assertEquals(ExampleTestClass.class, model.getForClass());
	}

	@Test
	void noModelByName() {
		  Assertions.assertThrows(NoModelException.class, () -> {
			  Model.getModel("NoSuchClass");
		  });
	}

	@Test
	void noModelByClass() {
		  Assertions.assertThrows(NoModelException.class, () -> {
			  Model.getModel(ModelTest.class);
		  });
	}

	@Test
	void fields() throws Exception {
		Model model = Model.register(ExampleTestClass.class);
		assertEquals(2, model.getFields().size());
		
		ModelField field = model.getFields().get(0);
		assertEquals("Name", field.getName());
		
		field = model.getFields().get(1);
		assertEquals("Date", field.getName());
	}

	@Test
	void getFieldByName() throws Exception {
		Model model = Model.register(ExampleTestClass.class);
		ModelField field = model.getField("name");
		assertEquals("Name", field.getName());
	}	
	
	@Test
	void createInstance() throws Exception {
		Model model = Model.register(ExampleTestClass.class);
		Object instance = model.createInstance();
		assertTrue(instance instanceof ExampleTestClass);
	}
	

	@Test
	void setField() throws Exception {
		Model model = Model.register(ExampleTestClass.class);
		ModelField nameField = model.getFields().get(0);
		
		ExampleTestClass instance = new ExampleTestClass();
		nameField.setEntryData(instance, "Freddy");
		assertEquals("Freddy", instance.getName());
	}
}
