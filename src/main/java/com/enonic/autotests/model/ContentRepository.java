package com.enonic.autotests.model;

import java.util.List;
import java.util.Map;

/**
 * 
 * 02.04.2013
 */
public class ContentRepository {

	private String name;

	private String defaultLanguage;

	private List<String> alowedTypes;

	private TopCategory topCategory;

	private Map<String, Object> properties;

	// TODO add permissions:
	// private principals

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getAlowedTypes() {
		return alowedTypes;
	}

	public void setAlowedTypes(List<String> alowedTypes) {
		this.alowedTypes = alowedTypes;
	}

	public TopCategory getTopCategory() {
		return topCategory;
	}

	public void setTopCategory(TopCategory topCategory) {
		this.topCategory = topCategory;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	/**
	 * 
	 *
	 */
	public static class TopCategory {
		private String name;
		private String type;
		private String description;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}
}
