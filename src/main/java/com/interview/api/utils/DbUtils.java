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

public class DbUtils {

	public static DataSource createDataSource() {
		URI dbUri;
		try {
			dbUri = new URI(System.getenv("DATABASE_URL"));
		} catch (URISyntaxException ex) {
			return null;
		}

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath()
				+ ":" + dbUri.getPort();

		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setUrl(dbUrl);
		basicDataSource.setUsername(username);
		basicDataSource.setPassword(password);

		return basicDataSource;
	}

	public static List<String> selectQuestions(final DataSource dataSource,
			final Category category) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		ResultSetHandler<List<String>> h = new ResultSetHandler<List<String>>() {
			public List<String> handle(ResultSet rs) throws SQLException {
				List<String> questions = new ArrayList<String>();

				while (rs.next()) {
					String question = rs.getString("question");
					questions.add(question);
				}

				return questions;
			}
		};
		List<String> questions = queryRunner
				.query("SELECT question, question_type FROM questions where category in(?, ?)",
						h, Category.ALL.getShortName(), category.getShortName());

		return questions;
	}
}
