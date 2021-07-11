package com.waracle.cakemgr.ui;

import java.io.PrintWriter;

public class HtmlLeaf extends HtmlElement {

	private String content;

	public HtmlLeaf(String name, String classes, String content) {
		super(name, classes);
		this.content = content;
	}

	@Override
	protected void doWrite(PrintWriter writer) {
		writer.print(content);
	}

}
