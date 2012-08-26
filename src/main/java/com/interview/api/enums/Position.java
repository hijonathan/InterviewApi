package com.interview.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum Position {

	ALL(null, "All", "all", "w"),
	MARKETER(null, "Marketer", "marketer", "W"),
	SOFTWARE_DEVELOPER(null, "Software Developer", "software_developer", "Q"),
	FINANCIAL_ANALYST(null, "Financial Analyst", "financial_analyst", "%"),
	CONSULTANT(null, "Consultant", "consultant", "O");
	
	private Position parent;
	private String displayName;
	private String shortName;
	private String icon;
	private Position(final Position parent, final String displayName, final String shortName, final String icon) {
		this.parent = parent;
		this.displayName = displayName;
		this.shortName = shortName;
		this.icon = icon;
	}
	
	public Position getParent() {
		return parent;
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
