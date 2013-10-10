package com.enonic.autotests.pages.adminconsole.content.search;

import java.util.List;

public class ContentSearchParams
{
	//General:
	private SearchWhere where;

	private String searchText;
	
	private List<String> contentTypeNames;
	//Metadata ...
	
    // Assignmen  ... TODO
	
	public static class Builder {

        private SearchWhere bWhere;

        private String bSearchText;
        private List<String> bContentTypeNames;

        public Builder() {
            super();
        }

        public Builder(SearchWhere where, String text) {
            super();
            this.bWhere = where;
            this.bSearchText = text;
        }

        public Builder searcText(String searchText) {
            this.bSearchText = searchText;
            return this;
        }

        public Builder where(SearchWhere where) {
            this.bWhere = where;
            return this;
        }

        public Builder contentTypes(List<String> contentTypeNames) {
            this.bContentTypeNames = contentTypeNames;
            return this;
        }

        public ContentSearchParams build() {
        	ContentSearchParams params = new ContentSearchParams();
        	params.searchText = bSearchText;
        	params.where = bWhere;
            return params;
        }
    }

    public static Builder with() {
        return new Builder();
    } 
	
	
	public SearchWhere getWhere()
	{
		return where;
	}
	public void setWhere(SearchWhere where)
	{
		this.where = where;
	}
	public String getSearchText()
	{
		return searchText;
	}
	public void setSearchText(String searchText)
	{
		this.searchText = searchText;
	}
	public List<String> getContentTypeNames()
	{
		return contentTypeNames;
	}
	public void setContentTypeNames(List<String> contentTypeNames)
	{
		this.contentTypeNames = contentTypeNames;
	}
	
	
}
