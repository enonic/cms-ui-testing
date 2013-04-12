package com.enonic.autotests.model;

public class FilesContent extends BaseAbstractContent {
	private String description;
	
	private String comment;// Draft (Offline)

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
