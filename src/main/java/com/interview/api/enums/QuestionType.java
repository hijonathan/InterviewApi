package com.interview.api.enums;

import java.util.HashMap;
import java.util.Map;


public enum QuestionType {

	GENERAL("general", 0, 2),
	COMPANY("company", 1, 1),
	BACKGROUND("background", 1, 2),
	PERSONAL("personal", 1, 1),
	TECHNICAL("technical", 2, 3);
	
	private String shortName;
	private int priority;
	private int num;
	private QuestionType(final String shortName, final Integer priority, final Integer number) {
		this.shortName = shortName;
		this.priority = priority;
		this.num = number;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public Integer getNum() {
		return num;
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
