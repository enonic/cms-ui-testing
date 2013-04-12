package com.enonic.autotests.testdata.contenttype;

import java.io.InputStream;
import java.util.Scanner;

import com.enonic.autotests.model.ContentHandler;
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
		

		ContentType ctype = new ContentType();
		ctype.setName(ctypeXML.getName());
		ctype.setDescription(ctypeXML.getDescription());
		ctype.setPathToCSS(ctypeXML.getPathToCSS());
		ContentHandler chandler = ContentHandler.findByValue(ctypeXML.getContentHandler());
		ctype.setContentHandler(chandler);
		if(ctypeXML.getCfgFile()!=null && !ctypeXML.getCfgFile().isEmpty()){
			InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("contenttype/"+ ctypeXML.getCfgFile());
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
		contentRepository.setSelectedTypes(cRepXML.getAllowedContentTypes());
		
		
		return contentRepository;
	}
	/**
	 * Converts xml-data to {@link TopCategory} instance.
	 * @param topCat  {@link TopCategoryXml} instance.
	 * @return {@link TopCategory} instance.
	 */
	private static TopCategory convertXmlDataToTopCategory(TopCategoryXml topCat){
		TopCategory topcategory = new TopCategory();
		topcategory.setName(topCat.getName());
		topcategory.setContentType(convertXmlDataToContentType(topCat.getContentType()));
		topcategory.setDescription(topCat.getDescription());
		return topcategory;
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
