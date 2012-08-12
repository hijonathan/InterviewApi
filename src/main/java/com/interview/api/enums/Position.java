package com.interview.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum Position {

	ALL("All", "all", "w"),
	MARKETER("Marketer", "marketer", "W"),
	SOFTWARE_DEVELOPER("Software Developer", "software_developer", "Q"),
	FINANCIAL_ANALYST("Financial Analyst", "financial_analyst", "%"),
	CONSULTANT("Consultant", "consultant", "O");
	
	private String displayName;
	private String shortName;
	private String icon;
	private Position(final String displayName, final String shortName, final String icon) {
		this.displayName = displayName;
		this.shortName = shortName;
		this.icon = icon;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public String getIcon() {
		return icon;
	}

	private static final Map<String, Position> BY_SHORT_NAME;
	static {
		Position[] positions = Position.values();
		BY_SHORT_NAME = new HashMap<String, Position>(positions.length);
		for (Position p : positions) {
			BY_SHORT_NAME.put(p.getShortName(), p);
		}
	}
	
	public static Position fromShortName(final String shortName) {
		return BY_SHORT_NAME.get(shortName);
	}
}
