package com.interview.api.utils;

import org.apache.commons.lang.StringUtils;

import com.interview.api.enums.Position;
import com.interview.api.enums.QuestionType;

public class Question {

	public static class TextAndAudio {
		private final String text;
		private final String audio;
		
		public TextAndAudio(final String text, final String audio) {
			this.text = text;
			this.audio = audio;
		}
		
		public String getText() {
			return text;
		}
		
		public String getAudio() {
			return audio;
		}
	}
	
	private final int id;
	private final TextAndAudio question;
	private final Position position;
	private final QuestionType questionType;
	private final Boolean isActive;
	private final TextAndAudio followUp;
	
	public Question(final int id, final TextAndAudio question, final Position position, final QuestionType questionType, final Boolean isActive, final TextAndAudio followUp) {
		this.id = id;
		this.question = question;
		this.position = position;
		this.questionType = questionType;
		this.isActive = isActive;
		this.followUp = followUp;
	}
	
	public int getId() {
		return id;
	}
	
	public TextAndAudio getQuestion() {
		return question;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public QuestionType getQuestionType() {
		return questionType;
	}
	
	public TextAndAudio getFollowUp() {
		return followUp;
	}
	
	public Boolean hasFollowUp() {
		return !StringUtils.isEmpty(followUp.getText());
	}
	
	public Boolean isActive() {
		return isActive == null ? false : isActive;
	}
}
