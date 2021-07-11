package com.waracle.cakemgr.ui;

import java.io.PrintWriter;

public class PageBuilder extends HtmlElement {

	private HtmlNode body;
	private HtmlNode head;

	public PageBuilder() {
		super("html", null);
	}
	
	public void write(PrintWriter writer) {
		writer.println("<!Doctype html>");
		super.write(writer);
	}

	@Override
	protected void doWrite(PrintWriter writer) {
		if (head != null) {
			head.write(writer);
		}
		if (body != null) {
			body.write(writer);
		}
	}
	
	public HtmlNode getHead() {
		if (head == null) {
			head = new HtmlNode("head", null);
		}
		return head;
	}
	
	public HtmlNode getBody() {
		if (body == null) {
			body = new HtmlNode("body", null);
		}
		return body;
	}

	public void setTitle(String title) {
		getHead().addLeaf("title", null, title);
	}

	public void addCssLink(String path) {
		getHead().addContent("<link rel=\"stylesheet\" type=\"text/css\" href=\"" +
				path + "\">");
	}

}
