package com.interview.api.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.interview.api.enums.Category;

public class DbUtils {
	
	public static DataSource createDataSource(final String host, final String dbname, final String username, final String password) {
		BasicDataSource pool = new BasicDataSource();
		pool.setUrl("jdbc:mysql://" + host + ":3306/" + dbname + "?autoReconnect=true&zeroDateTimeBehavior=convertToNull");
		pool.setDriverClassName("com.mysql.jdbc.Driver");
		pool.setUsername(username);
		pool.setPassword(password);
		pool.setValidationQuery("select 1");
		
		return pool;
	}
	
	public static List<String> selectQuestions(final DataSource dataSource, final Category category) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		ResultSetHandler<List<String>> h = new ResultSetHandler<List<String>>() {
		    public List<String> handle(ResultSet rs) throws SQLException {
		        List<String> questions = new ArrayList<String>();
		    
		        while(rs.next()) {
		        	String question = rs.getString("question");
		        	questions.add(question);
		        }

		        return questions;
		    }
		};
		List<String> questions = queryRunner.query("SELECT question, question_type FROM questions where category in(?, ?)", h, Category.ALL.getShortName(), category.getShortName());
		
		return questions;
	}
}
