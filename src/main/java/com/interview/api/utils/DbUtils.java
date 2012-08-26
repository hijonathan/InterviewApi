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

import com.interview.api.enums.Position;
import com.interview.api.enums.QuestionType;
import com.interview.api.resources.QuestionsForType;
import com.interview.api.utils.Question.TextAndAudio;

public class DbUtils {
	
	private static ResultSetHandler<List<Question>> questionsHandler = new ResultSetHandler<List<Question>>() {
	    public List<Question> handle(ResultSet rs) throws SQLException {
	        List<Question> list = new ArrayList<Question>();
	        while(rs.next()) {
	        	int id = rs.getInt("id");
	        	String question = rs.getString("question");
	        	QuestionType type = QuestionType.fromShortName(rs.getString("question_type"));
	        	Position position = Position.fromShortName(rs.getString("position"));
	        	String audio = rs.getString("audio");
	        	Boolean isActive = rs.getBoolean("active");
	        	String followUp = rs.getString("follow_up");
	        	String followUpAudio = rs.getString("follow_up_audio");
	        	list.add(new Question(id, new TextAndAudio(question, audio), position, type, isActive, new TextAndAudio(followUp, followUpAudio)));
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
	
	public static List<QuestionsForType> selectQuestions(final DataSource dataSource, final Position position) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		List<Question> result = queryRunner.query("SELECT * FROM questions where position in(?, ?) and active=1", questionsHandler, Position.ALL.getShortName(), position.getShortName());
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
	
	public static List<Question> selectAllQuestions(final DataSource dataSource) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		List<Question> result = queryRunner.query("SELECT * FROM questions", questionsHandler);
		return result;
	}
	
	public static void addQuestion(final DataSource dataSource, final String question, final Position position, final QuestionType questionType) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		queryRunner.update("INSERT INTO questions(question, position, question_type) VALUES(?,?,?)", question, position.getShortName(), questionType.getShortName());
	}

	public static Question getQuestion(final DataSource dataSource, final Integer id) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		List<Question> result = queryRunner.query("SELECT * FROM questions where id = ?", questionsHandler, id);
		return result.get(0);
	}
	
	public static void deleteQuestion(final DataSource dataSource, final Integer id) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		queryRunner.update("DELETE FROM questions WHERE id = ?", id);
	}
	
	public static void editQuestion(final DataSource dataSource, final Integer id, final String question) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		int numRows = queryRunner.update("UPDATE questions SET question = ? WHERE id = ?", question, id);
		System.out.println("updated " + numRows + " rows");
	}
	
	public static void editQuestion(final DataSource dataSource, final Integer id, final Position position) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		int numRows = queryRunner.update("UPDATE questions SET position = ? WHERE id = ?", position.getShortName(), id);
		System.out.println("updated " + numRows + " rows");
	}
	
	public static void editQuestion(final DataSource dataSource, final Integer id, final QuestionType questionType) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		int numRows = queryRunner.update("UPDATE questions SET question_type = ? WHERE id = ?", questionType.getShortName(), id);
		System.out.println("updated " + numRows + " rows");
	}
	
	public static void editQuestion(final DataSource dataSource, final Integer id, final Boolean active) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		int numRows = queryRunner.update("UPDATE questions SET active = ? WHERE id = ?", active, id);
		System.out.println("updated " + numRows + " rows");
	}

	public static void addAudio(final DataSource dataSource, final Integer id, final String audio, final Boolean isFollowUp) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		String query = "UPDATE questions SET audio = ? WHERE id = ?";
		if (isFollowUp) {
			query = "UPDATE questions SET follow_up_audio = ? WHERE id = ?";
		}
		int numRows = queryRunner.update(query, audio, id);
		System.out.println("updated " + numRows + " rows");
	}

	public static void addFollowUp(final DataSource dataSource, final Integer id, final String followUp) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(dataSource);
		int numRows = queryRunner.update("UPDATE questions SET follow_up = ? WHERE id = ?", followUp, id);
		System.out.println("updated " + numRows + " rows");
	}
}
