package com.waracle.cakemgr.ui;

import java.io.PrintWriter;

public abstract class HtmlElement {

	private final String name;
	private final String classes;
	private final StringBuilder attributes = new StringBuilder();
	
	public HtmlElement(String name, String classes) {
		this.name = name;
		this.classes = classes;
	}

	public void write(PrintWriter writer) {
		writer.print("<" + name);
		if (classes != null) {
			writer.print(" class=\"" + classes + "\"");
		}
		writer.print(attributes.toString());
		writer.print(">");
		doWrite(writer);
		writer.print("</" + name + ">");
	}

	protected abstract void doWrite(PrintWriter writer);

	public void addAttribute(String name, String value) {
		attributes.append(" ");
		attributes.append(name);
		attributes.append("=\"");
		attributes.append(value);
		attributes.append("\"");
	}


}
