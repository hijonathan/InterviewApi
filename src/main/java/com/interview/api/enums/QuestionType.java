package com.interview.api.enums;

import java.util.HashMap;
import java.util.Map;


public enum QuestionType {

	GENERAL("general", 0, 1),
	COMPANY("company", 1, 2),
	BACKGROUND("background", 2, 1),
	PERSONAL("personal", 3, 1),
	TECHNICAL("technical", 4, 3);
	
	private String shortName;
	private int priority;
	private int number;
	private QuestionType(final String shortName, final Integer priority, final Integer number) {
		this.shortName = shortName;
		this.priority = priority;
		this.number = number;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public Integer getPriority() {
		return priority;
	}

	private static final Map<String, QuestionType> BY_SHORT_NAME;
	static {
		QuestionType[] types = QuestionType.values();
		BY_SHORT_NAME = new HashMap<String, QuestionType>(types.length);
		for (QuestionType t : types) {
			BY_SHORT_NAME.put(t.getShortName(), t);
		}
	}
	
	public static QuestionType fromShortName(final String shortName) {
		return BY_SHORT_NAME.get(shortName);
	}
}
