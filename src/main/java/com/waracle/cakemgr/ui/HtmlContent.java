package com.waracle.cakemgr.ui;

import java.io.PrintWriter;

public class HtmlContent extends HtmlElement {

	private final String content;

	public HtmlContent(String content) {
		super(null, null);
		this.content = content;
	}

	@Override
	public void write(PrintWriter writer) {
		writer.print(content);
	}
	
	@Override
	protected void doWrite(PrintWriter writer) {
	}

}
