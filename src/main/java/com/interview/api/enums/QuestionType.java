package com.interview.api.enums;


public enum QuestionType {

	GENERAL("general", 0, 2),
	BACKGROUND("background", 1, 2),
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
}
