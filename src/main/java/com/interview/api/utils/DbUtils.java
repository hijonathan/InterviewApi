package com.interview.api.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.interview.api.enums.Category;
import com.interview.api.enums.QuestionType;
import com.interview.api.resources.QuestionsForType;

public class DbUtils {
	
	private static ResultSetHandler<List<Question>> questionsHandler = new ResultSetHandler<List<Question>>() {
	    public List<Question> handle(ResultSet rs) throws SQLException {
	        List<Question> list = new ArrayList<Question>();
	        while(rs.next()) {
	        	int id = rs.getInt("id");
	        	String question = rs.getString("question");
	        	QuestionType type = QuestionType.fromShortName(rs.getString("question_type"));
	        	Category category = Category.fromShortName(rs.getString("category"));
	        	list.add(new Question(id, question, category, type));
	        }

	        return list;
	    }
	};
	
	public static DataSource createDataSource() {
		URI dbUri;
		try {
			dbUri = new URI(System.getenv("CLEARDB_DATABASE_URL"));
		} catch (URISyntaxException e) {
			return null;
		}

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath() + "?autoReconnect=true";
        System.out.println(dbUrl);
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        basicDataSource.setMaxActive(10);
        basicDataSource.setInitialSize(2);
        basicDataSource.setMaxIdle(1);
        basicDataSource.setValidationQuery("select 1");
        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setMaxWait(60 * 1000);

        return basicDataSource;
	}
	
	public static List<QuestionsForType> selectQuestions(final DataSource dataSource, final Category category) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		List<Question> result = queryRunner.query("SELECT id, question, category, question_type FROM questions where category in(?, ?)", questionsHandler, Category.ALL.getShortName(), category.getShortName());
		Map<QuestionType, QuestionsForType> map = new HashMap<QuestionType, QuestionsForType>();
        for (Question  question : result) {
        	QuestionType type = question.getQuestionType();
        	if (!map.containsKey(type)) {
        		map.put(type, new QuestionsForType(type));
        	}
        	map.get(type).add(question);
        }
		return new ArrayList<QuestionsForType>(map.values());
	}
	
	public static Map<Category, List<Question>> selectAllQuestions(final DataSource dataSource) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		List<Question> result = queryRunner.query("SELECT id, question, category, question_type FROM questions", questionsHandler);
		Map<Category, List<Question>> questionsByCategory = new HashMap<Category, List<Question>>();
		for (Question question : result) {
			if (!questionsByCategory.containsKey(question.getCategory())) {
				questionsByCategory.put(question.getCategory(), new ArrayList<Question>());
			}
			questionsByCategory.get(question.getCategory()).add(question);
		}
		return questionsByCategory;
	}
	
	public static void addQuestion(final DataSource dataSource, final String question, final Category category, final QuestionType questionType) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		queryRunner.update("INSERT INTO questions(question, category, question_type) VALUES(?,?,?)", question, category.getShortName(), questionType.getShortName());
	}
}
