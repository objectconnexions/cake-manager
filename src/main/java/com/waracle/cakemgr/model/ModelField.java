package com.waracle.cakemgr.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModelField {

	private final String jsonName;
	private final String name;
	private final Method getField;
	private final Method setField;

	public ModelField(Class<? extends Object> cls, Method getField) {
		this.getField = getField;
		// TODO name should have spaces between parts of words (where camel case is used)
		name = getField.getName().substring(3);
		jsonName = Character.toLowerCase(getField.getName().charAt(3)) + getField.getName().substring(4);
		
		try {
			setField = cls.getMethod("set" + name, getField.getReturnType());
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ModelException("Unable to find set field property for " + name);
		}
	}
	
	public String getName() {
		return name;
	}

	public String getJsonName() {
		return jsonName;
	}

	public String getJsonValue(Object entity) {
		return String.valueOf(getEntryData(entity));
	}

	public Object getEntryData(Object entity) {
		try {
			Object result = getField.invoke(entity);
			return result;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ModelException("Unable to access field property " + jsonName, e);
		}
	}

	public void setEntryData(Object entity, Object value) {
		try {
			setField.invoke(entity, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ModelException("Unable to set field property " + jsonName, e);
		}
	}


}
