package com.interview.api.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.interview.api.utils.Question;

@Path("/questions")
@Produces({MediaType.APPLICATION_JSON})
public class QuestionsResource {

	@Inject
	private DataSource dataSource;
	
	@GET
	@Path("/{category}")
	public String getQuestions(@PathParam("category") String category) throws SQLException, IOException {
		List<QuestionsForType> result = DbUtils.selectQuestions(dataSource, Category.fromShortName(category));
		
		List<Question> questions = new ArrayList<Question>();
		Collections.sort(result, new Comparator<QuestionsForType>() {

			public int compare(QuestionsForType arg0, QuestionsForType arg1) {
				return arg0.getPriority() - arg1.getPriority();
			}
			
		});
		for (QuestionsForType t : result) {
			questions.addAll(t.getRandomQuestions());
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JsonGenerator generator = new JsonFactory().createJsonGenerator(baos, JsonEncoding.UTF8);
		writeQuestionsToJson(questions, generator);
		generator.close();
		
		return baos.toString();
	}
	
	private void writeQuestionsToJson(final List<Question> questions, final JsonGenerator generator) throws IOException {
		generator.writeStartArray();
		for (Question question : questions) {
			generator.writeStartObject();
			generator.writeNumberField("id", question.getId());
			generator.writeStringField("question", question.getQuestion());
			generator.writeStringField("category", question.getCategory().getShortName());
			generator.writeStringField("questionType", question.getQuestionType().getShortName());
			generator.writeEndObject();
		}
		generator.writeEndArray();
	}
	
	@GET
	@Path("/all")
	public String getAllQuestions() throws SQLException, IOException {
		List<Question> questions = DbUtils.selectAllQuestions(dataSource);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JsonGenerator generator = new JsonFactory().createJsonGenerator(baos, JsonEncoding.UTF8);
		writeQuestionsToJson(questions, generator);
		generator.close();
		return baos.toString();
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
	
	@GET
	@Path("/question-types")
	public String getAllQuestionTypes() throws IOException {
		QuestionType[] types = QuestionType.values();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JsonGenerator generator = new JsonFactory().createJsonGenerator(baos, JsonEncoding.UTF8);
		generator.writeStartArray();
		for (QuestionType t : types) {
			generator.writeStartObject();
			generator.writeStringField("shortName", t.getShortName());
			generator.writeNumberField("priority", t.getPriority());
			generator.writeNumberField("number", t.getNumber());
			generator.writeEndObject();
		}

		generator.writeEndArray();
		generator.close();
		
		return baos.toString();
	}

	@DELETE
	@Path("/{id}/delete")
	@Consumes("application/x-www-form-urlencoded")
	public String deleteQuestion(@PathParam("id") Integer id) throws SQLException {
		DbUtils.deleteQuestion(dataSource, id);
		return "success";
	}

	@POST
	@Path("/{id}/edit")
	@Consumes("application/x-www-form-urlencoded")
	public String editQuestion(@PathParam("id") Integer id, @FormParam("question") String question, @FormParam("category") String category, @FormParam("questionType") String questionType) throws SQLException {
		if (question != null) {
			DbUtils.editQuestion(dataSource, id, question);
		}
		if (category != null) {
			DbUtils.editQuestion(dataSource, id, Category.fromShortName(category));
		}
		if (questionType != null) {
			DbUtils.editQuestion(dataSource, id, QuestionType.fromShortName(questionType));
		}
		return "success";
	}
	
	@POST
	@Path("/{category}/{type}")
	@Consumes("application/x-www-form-urlencoded")
	public String addQuestion(@PathParam("category") String category, @PathParam("type") String type, @FormParam("question") String question) throws SQLException {
		DbUtils.addQuestion(dataSource, question, Category.fromShortName(category), QuestionType.fromShortName(type));
		return "success";
	}
}
