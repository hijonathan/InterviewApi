package com.interview.api.utils;

import org.apache.commons.lang.StringUtils;

import com.interview.api.enums.Position;
import com.interview.api.enums.QuestionType;

public class Question {

	private final int id;
	private final String question;
	private final Position position;
	private final QuestionType questionType;
	private final String audio;
	private final Boolean isActive;
	private final String followUp;
	
	public Question(final int id, final String question, final Position position, final QuestionType questionType, final String audio, final Boolean isActive, final String followUp) {
		this.id = id;
		this.question = question;
		this.position = position;
		this.questionType = questionType;
		this.audio = audio;
		this.isActive = isActive;
		this.followUp = followUp;
	}
	
	public int getId() {
		return id;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public QuestionType getQuestionType() {
		return questionType;
	}
	
	public String getAudio() {
		return audio;
	}
	
	public String getFollowUp() {
		return followUp;
	}
	
	public Boolean hasFollowUp() {
		return !StringUtils.isEmpty(followUp);
	}
	
	public Boolean isActive() {
		return isActive == null ? false : isActive;
	}
}
