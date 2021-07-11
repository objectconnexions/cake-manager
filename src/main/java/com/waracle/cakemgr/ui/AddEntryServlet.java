package com.waracle.cakemgr.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.waracle.cakemgr.HibernateUtil;
import com.waracle.cakemgr.model.Model;
import com.waracle.cakemgr.model.ModelField;
import com.waracle.cakemgr.rest.DataServlet;

@WebServlet(urlPatterns = "/ui/add/*", loadOnStartup = 1)
public class AddEntryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DataServlet.class);

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		final String className = req.getPathInfo().substring(1);
    	final Model model = Model.getModel(className);

    	
    	final PageBuilder page = new PageBuilder();
    	page.setTitle("Add " + model.getType());
    	page.addCssLink("/css/simple.css");
    	
    	final HtmlNode body = page.getBody();
    	body.addLeaf("H1", "", "Add " + model.getType());
    	
    	Object object = null;
    	
    	if (req.getParameterNames().hasMoreElements()) {
			object = model.createInstance();
			for (ModelField field : model.getFields()) {
				String value = req.getParameter(field.getJsonName());
				// TODO need to use the field type to convert the string entry to a Java type
				field.setEntryData(object, value);
			}
	    
			Session session = null;
	        try {
	        	session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                session.persist(object);
                LOG.info("adding entity " + object);
                session.getTransaction().commit();
		    	resp.sendRedirect("/ui/list/" + className);
	        } catch (HibernateException e) {
                LOG.warn("failed to save entity " + object, e);
	        	//resp.sendError(500, "Failed to save entity " + object);
                HtmlNode error = page.getBody().addNode("div", "error");
                error.addLeaf("p", null, "Invalid entry: " + e.getMessage());
			} finally {
				if (session != null) {
					session.close();
				}
			}

    	}
    	
		HtmlNode form = body.addNode("form", "add");
		for (ModelField field : model.getFields()) {
			HtmlNode fieldDiv = form.addNode("div", "field " + field.getJsonName());
			fieldDiv.addLeaf("label", null, field.getName());
			HtmlNode input = fieldDiv.addNode("input", null); 
			input.addAttribute("name", field.getJsonName());
			input.addAttribute("value", object == null ? "" : String.valueOf(field.getEntryData(object)));
		}
		form.addLeaf("button", null, "Save");

//		for (Object entity : list) {
//			fieldDiv = form.addNode("tr", null);
//			for (ModelField field : model.getFields()) {
//				HtmlNode cell = fieldDiv.addNode("td", "value");
//				valueHandler.handle(cell, model, field, field.getEntryData(entity));
//			}
//		}


		page.write(resp.getWriter());
	}

}
