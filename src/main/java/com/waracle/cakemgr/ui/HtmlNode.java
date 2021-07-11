package com.waracle.cakemgr.ui;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class HtmlNode extends HtmlElement {

	private final List<HtmlElement> children = new ArrayList<>();
	
	public HtmlNode(String name, String classes) {
		super(name, classes);
	}

	public HtmlElement addLeaf(String name, String classes, String content) {
		HtmlElement leaf = new HtmlLeaf(name, classes, content);
		children.add(leaf);
		return leaf;
	}

	public HtmlNode addNode(String name, String classes) {
		HtmlNode node = new HtmlNode(name, classes);
		children.add(node);
		return node ;
	}

	@Override
	protected void doWrite(PrintWriter writer) {
		for (HtmlElement element : children) {
			element.write(writer);
		}
	}

	public void addContent(String content) {
		children.add(new HtmlContent(content));
	}

}
