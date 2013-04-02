package com.enonic.autotests.validation.contenttype;


/**
 * 
 *
 * 01.04.2013
 */
public class ContentTypeValidateHelper {

	public static String CONTENTTYPE_TAG = "contenttype";
	public static String INDEXPARAMETERS_TAG = "indexparameters"; 
	
	/**
	 * TODO checkout another implementation of xml-content validation .
	 * 
	 * @param cfgContent
	 * @param tagNames
	 * @return
	 */
	public static boolean  validateConfigurationContent(Object cfgContent, String ... tagNames){
		boolean result=true;
		if(cfgContent == null){
			throw new IllegalArgumentException("Configuration content should not be equals null!");
		}
		String content = cfgContent.toString();
		if(content.isEmpty()){
			throw new IllegalArgumentException("Configuration content should not be empty!");
		}
		for(String tag: tagNames){
			result &= content.contains(tag);
		}
		return result;
	}
}
