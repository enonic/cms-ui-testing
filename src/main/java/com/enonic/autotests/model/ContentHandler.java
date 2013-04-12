package com.enonic.autotests.model;

import java.util.HashMap;
import java.util.Map;

public enum ContentHandler {

	FILES("Files"), IMAGES("Images"), CUSTOM_CONTENT("Custom content");
	private String name;

	public String getName() {
		return name;
	}

	private final static Map<String, ContentHandler> map = new HashMap<String, ContentHandler>();
	static {
		for (ContentHandler handler : values()) {
			map.put(handler.name, handler);
		}
	}

	private ContentHandler(String name) {
		this.name = name;
	}

	public static ContentHandler findByValue(String value) {
		return value != null ? map.get(value) : null;
	}
	
	
}
