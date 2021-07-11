package com.waracle.cakemgr.ui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PageBuilderTest extends AbstractHtmlBuilderTest {

	private PageBuilder page;

	@BeforeEach
	void setUp() throws Exception {
		page = new PageBuilder();
	}

	@Test
	void basicPage() {
		assertContent(page, "<!Doctype html><html></html>");
	}
	
	@Test
	void addHead() throws Exception {
		page.getHead();
		assertContent(page, "<!Doctype html><html><head></head></html>");
	}
	
	@Test
	void sameHead() throws Exception {
		HtmlNode head = page.getHead();
		HtmlNode nextHead = page.getHead();
		assertEquals(head, nextHead);
	}
	
	@Test
	void addTitle() throws Exception {
		page.setTitle("Example");
		assertContent(page, "<!Doctype html><html><head><title>Example</title></head></html>");
	}
	
	@Test
	void addCssLink() throws Exception {
		page.addCssLink("path/example.css");
		assertContent(page, "<!Doctype html><html><head><link rel=\"stylesheet\" "
				+ "type=\"text/css\" href=\"path/example.css\"></head></html>");
	}
	
	
	
	@Test
	void addBody() throws Exception {
		page.getBody();
		assertContent(page, "<!Doctype html><html><body></body></html>");
	}
	
	@Test
	void sameBody() throws Exception {
		HtmlNode body = page.getBody();
		HtmlNode nextBody = page.getBody();
		assertEquals(body, nextBody);
	}
	
	@Test
	void addBodyWithTitle() throws Exception {
		HtmlNode body = page.getBody();
		body.addLeaf("h2", null, "Title");
		assertContent(page, "<!Doctype html><html><body><h2>Title</h2></body></html>");
	}

}
