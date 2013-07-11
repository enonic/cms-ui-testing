package com.enonic.autotests.testdata.content;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "contentRepositories")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "name", "defaultLanguage","topCategory","caseInfo","categories"}, name = "contentRepository")
public class ContentRepositoryXml {

	private String name;
	private String defaultLanguage;
	private TopCategoryXml topCategory;
	private String caseInfo;
	
	@XmlElementWrapper(name="categories")
    @XmlElement(name="category")
	private List<ContentCategoryXml> categories;
	
	//private List<String>allowedContentTypes;
	
	//@XmlElementWrapper(name="allowedTypes")
    //@XmlElement(name="type")
	
	
	public List<ContentCategoryXml> getCategories()
	{
		return categories;
	}
	public void setCategories(List<ContentCategoryXml> categories)
	{
		this.categories = categories;
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefaultLanguage() {
		return defaultLanguage;
	}
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}
	public TopCategoryXml getTopCategory() {
		return topCategory;
	}
	public void setTopCategory(TopCategoryXml topCategory) {
		this.topCategory = topCategory;
	}
//	public List<String> getAllowedContentTypes() {
//		return allowedContentTypes;
//	}
//	public void setAllowedContentTypes(List<String> allowedContentTypes) {
//		this.allowedContentTypes = allowedContentTypes;
//	}
	
	public String getCaseInfo() {
		return caseInfo;
	}
	public void setCaseInfo(String caseInfo) {
		this.caseInfo = caseInfo;
	}
	
}
