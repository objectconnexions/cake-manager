package com.waracle.cakemgr.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;

class AbstractHtmlBuilderTest {

	private StringWriter stringWriter;
	private PrintWriter writer;

	@BeforeEach
	void setUpWriter() throws Exception {
		stringWriter = new StringWriter();
		writer = new PrintWriter(stringWriter );
	}
	
	protected void assertContent(final HtmlElement element,  final String content) {
		element.write(writer);
		assertEquals(content, 
				stringWriter.getBuffer().toString().replaceAll("\n", ""));
	}

}
