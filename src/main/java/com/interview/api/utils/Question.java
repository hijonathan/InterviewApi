package com.interview.api.utils;

import com.interview.api.enums.Category;
import com.interview.api.enums.QuestionType;

public class Question {

	private final int id;
	private final String question;
	private final Category category;
	private final QuestionType questionType;
	
	public Question(final int id, final String question, final Category category, final QuestionType questionType) {
		this.id = id;
		this.question = question;
		this.category = category;
		this.questionType = questionType;
	}
	
	public int getId() {
		return id;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public QuestionType getQuestionType() {
		return questionType;
	}
}
