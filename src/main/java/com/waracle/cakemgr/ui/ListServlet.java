package com.waracle.cakemgr.ui;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.waracle.cakemgr.CakeEntity;
import com.waracle.cakemgr.HibernateUtil;
import com.waracle.cakemgr.model.Model;
import com.waracle.cakemgr.model.ModelField;

@WebServlet(urlPatterns = "/ui/list/*", loadOnStartup = 1)
public class ListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ValueHandler valueHandler;
	
	@Override
	public void init() throws ServletException {
		// TODO this should be an interface and injected to make it changeable
		valueHandler = new ValueHandler();
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		final String className = req.getPathInfo().substring(1);
    	final Model model = Model.getModel(className);

		final Session session = HibernateUtil.getSessionFactory().openSession();
		final List<Object> list = session.createCriteria(model.getForClass()).list();
		session.close();
		
		final PageBuilder page = new PageBuilder();
		page.setTitle(model.getType() + "s");
		page.addCssLink("/css/simple.css");
		
		final HtmlNode body = page.getBody();
		body.addLeaf("H1", "", model.getType());
		
		body.addNode("p", null).
			addLeaf("a", null, "Add " + model.getType()).
			addAttribute("href", "/ui/add/" + model.getType());
		
		HtmlNode table = body.addNode("table", null);
		HtmlNode row = table.addNode("tr", null);
		for (ModelField field : model.getFields()) {
			row.addLeaf("th", "label", field.getJsonName());
		}

		for (Object entity : list) {
			row = table.addNode("tr", null);
			for (ModelField field : model.getFields()) {
				HtmlNode cell = row.addNode("td", "value");
				valueHandler.handle(cell, model, field, field.getEntryData(entity));
			}
		}


		page.write(resp.getWriter());
	}

}
