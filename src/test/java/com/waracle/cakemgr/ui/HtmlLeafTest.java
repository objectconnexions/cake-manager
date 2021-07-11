package com.waracle.cakemgr.ui;

import org.junit.jupiter.api.Test;

class HtmlLeafTest extends AbstractHtmlBuilderTest {

	@Test
	void simpleLeaf() {
		HtmlLeaf leaf = new HtmlLeaf("h1", null, "Example");
		assertContent(leaf, "<h1>Example</h1>");
	}

	@Test
	void leafWithClasses() {
		HtmlLeaf leaf = new HtmlLeaf("h1", "example, item", "Example");
		assertContent(leaf, "<h1 class=\"example, item\">Example</h1>");
	}

}
