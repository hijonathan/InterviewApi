package com.interview.api.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
	
	private static ResultSetHandler<Collection<QuestionsForType>> resultSetHandler = new ResultSetHandler<Collection<QuestionsForType>>() {
	    public Collection<QuestionsForType> handle(ResultSet rs) throws SQLException {
	        Map<QuestionType, QuestionsForType> map = new HashMap<QuestionType, QuestionsForType>();
	        while(rs.next()) {
	        	String question = rs.getString("question");
	        	QuestionType type = QuestionType.fromShortName(rs.getString("question_type"));
	        	if (!map.containsKey(type)) {
	        		map.put(type, new QuestionsForType(type));
	        	}
	        	map.get(type).add(question);
	        }

	        return map.values();
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
	
	public static List<String> selectQuestions(final DataSource dataSource, final Category category) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		Collection<QuestionsForType> result = queryRunner.query("SELECT question, question_type FROM questions where category in(?, ?)", resultSetHandler, Category.ALL.getShortName(), category.getShortName());
		List<String> questions = new ArrayList<String>();
		List<QuestionsForType> sorted = new ArrayList<QuestionsForType>(result);
		Collections.sort(sorted, new Comparator<QuestionsForType>() {

			public int compare(QuestionsForType arg0, QuestionsForType arg1) {
				return arg0.getPriority() - arg1.getPriority();
			}
			
		});
		for (QuestionsForType t : result) {
			questions.addAll(t.getRandomQuestions());
		}
		return questions;
	}
	
	public static List<String> selectAllQuestions(final DataSource dataSource) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		Collection<QuestionsForType> result = queryRunner.query("SELECT question, question_type FROM questions", resultSetHandler);
		List<String> questions = new ArrayList<String>();
		for (QuestionsForType t : result) {
			questions.addAll(t.getAllQuestions());
		}
		return questions;
	}
	
	public static void addQuestion(final DataSource dataSource, final String question, final Category category, final QuestionType questionType) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		queryRunner.update("INSERT INTO questions(question, category, question_type) VALUES(?,?,?)", question, category.getShortName(), questionType.getShortName());
	}
}
