package com.waracle.cakemgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.waracle.cakemgr.model.Model;

@WebServlet(urlPatterns = "/cakes", loadOnStartup = 1)
public class CakeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CakeServlet.class);

	@Override
    public void init() throws ServletException {
        LOG.debug("init started");
        LOG.info("downloading cake json");
        try (InputStream inputStream = new URL("https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json").openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                line = reader.readLine();
            }

            LOG.debug("parsing cake json");
            JsonParser parser = new JsonFactory().createParser(buffer.toString());
            if (JsonToken.START_ARRAY != parser.nextToken()) {
                throw new Exception("bad token");
            }

            CakeEntity cakeEntity = null;
            JsonToken nextToken = parser.nextToken();
            while(nextToken == JsonToken.START_OBJECT) {
                LOG.debug("creating cake entity");

                /*
                 * TODO there are multiple records for the same cake. I assume that these are 
                 * updates to correct the image URLs, not all of which resolve to valid 
                 * resources. This loop should be changed check the URL, though an attempt to
                 * connect, and if it does to update any existing entity's URL.
                 * 
                 * If the entity already exists then it should not attempt to add another, same
                 * entity to the database as this gives an error.
                 */
				cakeEntity = new CakeEntity();
                LOG.debug(parser.nextFieldName());
                cakeEntity.setTitle(parser.nextTextValue());

                LOG.debug("  " + parser.nextFieldName());
                cakeEntity.setDescription(parser.nextTextValue());

                LOG.debug("  " + parser.nextFieldName());
                cakeEntity.setImage(parser.nextTextValue());

                persist(cakeEntity);

                nextToken = parser.nextToken();
                LOG.debug("  ignored token " + nextToken);

                nextToken = parser.nextToken();
                LOG.debug("  ignored token " + nextToken);
            }
            
            
            ExampleEntity example = new ExampleEntity();
            example.setDate(LocalDate.now());
            example.setName("Freddy");
            example.setCake(cakeEntity);
            persist(example);

        } catch (Exception ex) {
            throw new ServletException(ex);
        }
        
        
        LOG.info("init finished");
        
        
        Model.register(CakeEntity.class, "cake");
        Model.register(ExampleEntity.class, "example");
   }

	private void persist(Object object) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
		    session.beginTransaction();
		    session.persist(object);
		    LOG.debug("  adding cake entity");
		    session.getTransaction().commit();
		} catch (ConstraintViolationException ex) {

		}
		session.close();
	}

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CakeEntity> list = session.createCriteria(CakeEntity.class).list();

        // JSON was not well formed. Delegating process to library
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        for (CakeEntity entity : list) {
        	 ObjectNode node = mapper.createObjectNode();
        	 node.put("title", entity.getTitle());
        	 node.put("desc", entity.getDescription());
        	 node.put("image", entity.getImage());
        	arrayNode.add(node);
        }
        
        try {
	        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
	        resp.getWriter().println(json);
        } catch (JsonProcessingException e) {
        	throw new ServletException("Failed to create JSON structure", e);
        }
    }

}
