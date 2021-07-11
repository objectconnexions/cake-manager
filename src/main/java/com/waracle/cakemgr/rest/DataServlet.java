package com.waracle.cakemgr.rest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.waracle.cakemgr.HibernateUtil;
import com.waracle.cakemgr.model.Model;
import com.waracle.cakemgr.model.ModelField;


@WebServlet(urlPatterns = "/data/*", loadOnStartup = 1)
public class DataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DataServlet.class);
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String className = req.getPathInfo().substring(1);
    	Model model = Model.getModel(className);

    	String jsonString= req.getReader().lines().collect(Collectors.joining());
        JsonParser parser = new JsonFactory().createParser(jsonString);
        JsonToken nextToken = parser.nextToken();
        if (nextToken == JsonToken.START_OBJECT) {
        	Object newObject = model.createInstance();
	        String fieldName;
	        while((fieldName = parser.nextFieldName()) != null) {
	        	String fieldValue = parser.nextTextValue();
	        	ModelField field = model.getField(fieldName);
	        	field.setEntryData(newObject, fieldValue);
	        }
	        
	        Session session = null;
	        try {
	        	session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                session.persist(newObject);
                LOG.info("adding entity " + newObject);
                session.getTransaction().commit();
		    	resp.setStatus(200);
	        } catch (HibernateException e) {
                LOG.warn("failed to save entity " + newObject, e);
	        	resp.sendError(500, "Failed to save entity " + newObject);
			} finally {
				if (session != null) {
					session.close();
				}
			}
        }
        
	}
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String className = req.getPathInfo().substring(1);
    	Model model = Model.getModel(className);

    	Session session = HibernateUtil.getSessionFactory().openSession();
    	List<Object> list = session.createCriteria(model.getForClass()).list();
    	session.close();

    	ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        for (Object entity : list) {
        	 ObjectNode node = mapper.createObjectNode();
        	 for (ModelField field : model.getFields()) {
            	 node.put(field.getJsonName(), field.getJsonValue(entity));        		 
        	 }
        	arrayNode.add(node);
        }
        
        
        resp.setContentType("application/json");
        
        JsonNode jsonNode = arrayNode;
        
        if (jsonNode != null) {
	        String jsonString;
	        try {
				jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
	        } catch (JsonProcessingException e) {
	        	throw new ServletException("Failed to create JSON structure", e);
	        }
	        resp.getWriter().println(jsonString);
        }
        
    }

}
