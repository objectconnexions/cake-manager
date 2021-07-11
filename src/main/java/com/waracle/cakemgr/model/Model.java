package com.waracle.cakemgr.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Model {

	private static final Map<String, Model> modelsByName = new HashMap<>();
	private static final Map<Class<? extends Object>, Model> modelsByClass = new HashMap<>();

	public static Model register(Class<? extends Object> cls) {
		String name = cls.getSimpleName();
		return register(cls, name);
	}

	public static Model register(Class<? extends Object> cls, String name) {
		final Model model;
		if (modelsByClass.containsKey(cls)) {
			model = modelsByClass.get(cls);
		} else {
			model = new Model(cls, name);
			modelsByClass.put(cls, model);
			modelsByName.put(name, model);
		}
		return model;
	}

	public static Model getModel(String name) {
		final Model model = modelsByName.get(name);
		if (model == null) {
			throw new NoModelException("No model available for " + name);
		}
		return model;
	}

	public static Model getModel(Class<? extends Object> cls) {
		final Model model = modelsByClass.get(cls);
		if (model == null) {
			throw new NoModelException("No model available for " + cls.getName());
		}
		return model;
	}
	
	private final Class<? extends Object> cls;
	private final String type;
	private final List<ModelField> fields = new ArrayList<>();

	private Model(Class<? extends Object> cls, String name) {
		this.cls = cls;
		this.type = name;
		reflect();
	}

	private void reflect() {
		/*
		 * Find all properties where there is a get and set method
		 * for the same name.
		 */
		List<Method> getFields = Arrays.stream(cls.getMethods()).
			filter(m -> {
				String name = m.getName();
				if (name.startsWith("get")) {
					String expect = "s" + name.substring(1);
					return Arrays.stream(cls.getMethods()).
							anyMatch(n -> n.getName().equals(expect));
					
					/*
					 *  TODO also check for 1) valid return types and 2) the same type
					 *  in get and set. 
					 */
				} else {
					return false;
				}
			}).
			collect(Collectors.toList());
		
		getFields.forEach(f -> {
			fields.add(new ModelField(cls, f));
		});
	}

	public List<ModelField> getFields() {
		return fields;
	}

	public String getType() {
		return type;
	}

	public Class<? extends Object> getForClass() {
		return cls;
	}

	public Object createInstance() {
		try {
			Constructor<? extends Object> constructor = cls.getDeclaredConstructor();
			return constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new ModelException("Could not create instance of " + type);
		}
	}

	public ModelField getField(String fieldName) {
		return fields.stream().
				filter(f -> f.getJsonName().equals(fieldName)).
				findFirst().
				orElseThrow();
	}

}
