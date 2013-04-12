package com.enonic.autotests.model;

/**

 *
 * 12.04.2013
 */
public class ContentProperties {

	private String name;
	
	private String created;
	
	private String owner;
	
	private String language;
	
	private String priority;
	
	private String key;
	
	private String contentType;
	
	private Assignment assignment;
	
		
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public static class Assignment {
		private String assignee;
		private String assigner;
		private String dueDate;
		
		public String getAssignee() {
			return assignee;
		}
		public void setAssignee(String assignee) {
			this.assignee = assignee;
		}
		public String getAssigner() {
			return assigner;
		}
		public void setAssigner(String assigner) {
			this.assigner = assigner;
		}
		public String getDueDate() {
			return dueDate;
		}
		public void setDueDate(String dueDate) {
			this.dueDate = dueDate;
		}
	}
}
