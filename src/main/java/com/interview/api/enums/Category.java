package com.interview.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum Category {

	ALL("All", "all"),
	MARKETING("Marketing", "marketing"),
	SOFTWARE_DEVELOPER("Software Developer", "software_developer");
	
	private String displayName;
	private String shortName;
	private Category(final String displayName, final String shortName) {
		this.displayName = displayName;
		this.shortName = shortName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getShortName() {
		return shortName;
	}

	private static final Map<String, Category> BY_SHORT_NAME;
	static {
		Category[] categories = Category.values();
		BY_SHORT_NAME = new HashMap<String, Category>(categories.length);
		for (Category c : categories) {
			BY_SHORT_NAME.put(c.getShortName(), c);
		}
	}
	
	public static Category fromShortName(final String shortName) {
		return BY_SHORT_NAME.get(shortName);
	}
}
