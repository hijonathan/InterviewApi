package com.interview.api.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

import com.google.inject.Inject;
import com.interview.api.enums.Category;
import com.interview.api.enums.QuestionType;
import com.interview.api.utils.DbUtils;

@Path("/questions")
@Produces({MediaType.APPLICATION_JSON})
public class QuestionsResource {

	@Inject
	private DataSource dataSource;
	
	@GET
	@Path("/{category}")
	public String getQuestions(@PathParam("category") String category) throws SQLException, IOException {
		List<String> questions = DbUtils.selectQuestions(dataSource, Category.fromShortName(category));
		
		return writeQuestionsToJson(questions);
	}
	
	private String writeQuestionsToJson(final List<String> questions) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JsonGenerator generator = new JsonFactory().createJsonGenerator(baos, JsonEncoding.UTF8);
		generator.writeStartArray();
		for (String question : questions) {
			generator.writeString(question);
		}
		generator.writeEndArray();
		generator.close();
		
		return baos.toString();
	}
	
	@GET
	@Path("/all")
	public String getAllQuestions() throws SQLException, IOException {
		List<String> questions = DbUtils.selectAllQuestions(dataSource);
		
		return writeQuestionsToJson(questions);
	}
	
	@GET
	@Path("/categories")
	public String getAllCategories() throws IOException {
		Category[] categories = Category.values();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JsonGenerator generator = new JsonFactory().createJsonGenerator(baos, JsonEncoding.UTF8);
		generator.writeStartArray();
		for (Category c : categories) {
			generator.writeStartObject();
			generator.writeStringField("displayName", c.getDisplayName());
			generator.writeStringField("shortName", c.getShortName());
			generator.writeEndObject();
		}

		generator.writeEndArray();
		generator.close();
		
		return baos.toString();
	}
	
	@POST
	@Path("/{category}/{type}")
	@Consumes("application/x-www-form-urlencoded")
	public void addQuestion(@PathParam("category") String category, @PathParam("type") String type, @FormParam("question") String question) throws SQLException {
		
		DbUtils.addQuestion(dataSource, question, Category.fromShortName(category), QuestionType.fromShortName(type));
	}
}
