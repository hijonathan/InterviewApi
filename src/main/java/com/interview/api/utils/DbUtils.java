package com.interview.api.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.interview.api.enums.Category;
import com.interview.api.enums.QuestionType;

public class DbUtils {
	
	private static ResultSetHandler<List<String>> resultSetHandler = new ResultSetHandler<List<String>>() {
	    public List<String> handle(ResultSet rs) throws SQLException {
	        List<String> questions = new ArrayList<String>();
	    
	        while(rs.next()) {
	        	String question = rs.getString("question");
	        	questions.add(question);
	        }

	        return questions;
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
        String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
	}
	
	public static List<String> selectQuestions(final DataSource dataSource, final Category category) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		List<String> questions = queryRunner.query("SELECT question, question_type FROM questions where category in(?, ?)", resultSetHandler, Category.ALL.getShortName(), category.getShortName());
		
		return questions;
	}
	
	public static List<String> selectAllQuestions(final DataSource dataSource) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		List<String> questions = queryRunner.query("SELECT question, question_type FROM questions", resultSetHandler);
		
		return questions;
	}
	
	public static void addQuestion(final DataSource dataSource, final String question, final Category category, final QuestionType questionType) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		queryRunner.update("INSERT INTO questions(question, category, question_type) VALUES(?,?,?)", question, category.getShortName(), questionType.getShortName());
	}
}
