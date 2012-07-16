package com.interview.api.resources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.interview.api.enums.QuestionType;

public class QuestionsForType {

	private final QuestionType type;
	private final List<String> questions;
	
	public QuestionsForType(final QuestionType type) {
		this.type = type;
		this.questions = new ArrayList<String>();
	}
	
	public void add(final String question) {
		questions.add(question);
	}
	
	public List<String> getRandomQuestions() {
		int numberOfQuestions = type.getNumber();
		int size = questions.size();
		Set<Integer> indices = new HashSet<Integer>(numberOfQuestions);
		List<String> randomQuestions = new ArrayList<String>(numberOfQuestions);
		while (randomQuestions.size() < numberOfQuestions) {
			int index = RandomUtils.nextInt(size);
			while (indices.contains(index)) {
				index = RandomUtils.nextInt(size);
			}
			randomQuestions.add(questions.get(index));
			indices.add(index);
		}
		return randomQuestions;
	}

	public int getPriority() {
		return type.getPriority();
	}
	
	public List<String> getAllQuestions() {
		return questions;
	}
}
