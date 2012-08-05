package com.interview.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum Position {

	ALL("All", "all"),
	MARKETER("Marketer", "marketer"),
	SOFTWARE_DEVELOPER("Software Developer", "software_developer"),
	FINANCIAL_ANALYST("Financial Analyst", "financial_analyst"),
	CONSULTANT("Consultant", "consultant");
	
	private String displayName;
	private String shortName;
	private Position(final String displayName, final String shortName) {
		this.displayName = displayName;
		this.shortName = shortName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getShortName() {
		return shortName;
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
