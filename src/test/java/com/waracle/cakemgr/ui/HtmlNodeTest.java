package com.waracle.cakemgr.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HtmlNodeTest extends AbstractHtmlBuilderTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void addLeaves() {
		HtmlNode node = new HtmlNode("ul", "example");
		node.addLeaf("li", null, "One");
		node.addLeaf("li", null, "Two");
		
		assertContent(node, "<ul class=\"example\"><li>One</li><li>Two</li></ul>");
	}

	@Test
	void addNodes() {
		HtmlNode node = new HtmlNode("div", null);
		node.addNode("div", "example");
		node.addNode("ul", null);
		
		assertContent(node, "<div><div class=\"example\"></div><ul></ul></div>");
	}

	@Test
	void addContent() {
		HtmlNode node = new HtmlNode("p", null);
		node.addContent("Example text");
		assertContent(node, "<p>Example text</p>");		
	}

	@Test
	void addAttribute() {
		HtmlNode node = new HtmlNode("img", null);
		node.addAttribute("src", "path/to/image");
		assertContent(node, "<img src=\"path/to/image\"></img>");		
	}
}
