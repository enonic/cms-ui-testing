package com.enonic.autotests.model;

import java.util.List;

public class CustomContent {
	

	public static class InputModel{
		private String name;
		private String type;
		private String required;
		private List<String> options;
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
		public String getRequired() {
			return required;
		}
		public void setRequired(String required) {
			this.required = required;
		}
		public List<String> getOptions() {
			return options;
		}
		public void setOptions(List<String> options) {
			this.options = options;
		}
	}
}
