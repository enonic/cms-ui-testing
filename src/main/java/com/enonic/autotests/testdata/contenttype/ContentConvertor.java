package com.enonic.autotests.testdata.contenttype;

import java.io.InputStream;
import java.util.Scanner;

import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentRepository.TopCategory;
import com.enonic.autotests.model.ContentType;

public class ContentConvertor {
 
	/**
	 * Converts from {@link ContentTypeXml} to {@link ContentType}.
	 * 
	 * @param ctypeXML
	 * @return {@link ContentType} instance.
	 */
	public static ContentType convertXmlDataToContentType(ContentTypeXml ctypeXML) {
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("contenttype/"+ ctypeXML.getCfgFile());

		ContentType ctype = new ContentType();
		ctype.setName(ctypeXML.getName());
		ctype.setDescription(ctypeXML.getDescription());
		ctype.setPathToCSS(ctypeXML.getPathToCSS());
		ctype.setContentHandler(ctypeXML.getContentHandler());
		if(ctypeXML.getCfgFile()!=null && !ctypeXML.getCfgFile().isEmpty()){
			ctype.setConfiguration(readConfiguration(in));
		}
		return ctype;
	}
	/**
	 * Converts xml-data to {@link ContentRepository} instance.
	 * @param cRepXML {@link ContentRepositoryXml} instance.
	 * @return {@link ContentRepository} instance.
	 */
	public static ContentRepository convertXmlDataToContentRepository(ContentRepositoryXml cRepXML) {
		ContentRepository contentRepository = new ContentRepository();
		contentRepository.setName(cRepXML.getName());
		contentRepository.setDefaultLanguage(cRepXML.getDefaultLanguage());
		contentRepository.setTopCategory(convertXmlDataToTopCategory(cRepXML.getTopCategory()));
		contentRepository.setAlowedTypes(cRepXML.getAllowedContentTypes());
		
		
		return contentRepository;
	}
	/**
	 * Converts xml-data to {@link TopCategory} instance.
	 * @param topCat  {@link TopCategoryXml} instance.
	 * @return {@link TopCategory} instance.
	 */
	private static TopCategory convertXmlDataToTopCategory(TopCategoryXml topCat){
		TopCategory tc = new TopCategory();
		tc.setName(topCat.getName());
		tc.setType(topCat.getContentType());
		tc.setDescription(topCat.getDescription());
		return tc;
	}
	
	/**
	 * Reads data in XML for ContentType configuration Text Area.
	 * @param in {@link InputStream} instance.
	 * @return configuration as String.
	 */
	private static String readConfiguration(InputStream in){
		StringBuilder sb = new StringBuilder();
	    Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
           
        }		
        return sb.toString();
	}
}
