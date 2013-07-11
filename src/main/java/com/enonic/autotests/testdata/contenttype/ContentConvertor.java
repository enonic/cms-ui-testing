package com.enonic.autotests.testdata.contenttype;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentRepository.TopCategory;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.FileContentInfo;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.testdata.content.AbstractContentXml;
import com.enonic.autotests.testdata.content.ContentCategoryXml;
import com.enonic.autotests.testdata.content.ContentRepositoryXml;
import com.enonic.autotests.testdata.content.FileContentXml;
import com.enonic.autotests.testdata.content.ImageContentXml;
import com.enonic.autotests.testdata.content.TopCategoryXml;

public class ContentConvertor
{
	
	public static Content<?> convertXmlDataToContent(AbstractContentXml xmlContent)
	{
		if (xmlContent instanceof FileContentXml)
		{
			Content<FileContentInfo> fileContent = new Content<>();
			FileContentInfo fileContentTabInfo = new FileContentInfo();
			fileContentTabInfo.setComment(((FileContentXml) xmlContent).getComments());
			fileContentTabInfo.setDescription(((FileContentXml) xmlContent).getDescription());
			fileContentTabInfo.setPathToFile(((FileContentXml) xmlContent).getPathToFile());fileContentTabInfo.setPathToFile(((FileContentXml) xmlContent).getPathToFile());
			
			fileContent.setContentTab(fileContentTabInfo);
			
			
            return fileContent;
		}
		if (xmlContent instanceof ImageContentXml)
		{
			Content<ImageContentInfo> imageContent = new Content<>();
			
			ImageContentInfo imageContentTabInfo = new ImageContentInfo();
			imageContentTabInfo.setComment(((ImageContentXml) xmlContent).getComments());
			imageContentTabInfo.setDescription(((ImageContentXml) xmlContent).getDescription());
			imageContentTabInfo.setPathToFile(((ImageContentXml) xmlContent).getPathToFile());
			imageContentTabInfo.setPhotographerEmail(((ImageContentXml) xmlContent).getPhotographerEmail());
			imageContentTabInfo.setPhotographerName(((ImageContentXml) xmlContent).getPhotographerName());
			
			
			imageContent.setContentTab(imageContentTabInfo);
            return imageContent;
		}
		
		return null;
		
	}

	/**
	 * Converts from {@link ContentTypeXml} to {@link ContentType}.
	 * 
	 * @param ctypeXML
	 * @return {@link ContentType} instance.
	 */
	public static ContentType convertXmlDataToContentType(ContentTypeXml ctypeXML)
	{

		ContentType ctype = new ContentType();
		ctype.setName(ctypeXML.getName());
		ctype.setDescription(ctypeXML.getDescription());
		ctype.setPathToCSS(ctypeXML.getPathToCSS());
		ContentHandler chandler = ContentHandler.findByValue(ctypeXML.getContentHandler());
		if(chandler == null)
		{
			throw new IllegalArgumentException("Wrong name of ContentHandler specified:" +ctypeXML.getContentHandler());
		}
		ctype.setContentHandler(chandler);
		if(!chandler.equals(ContentHandler.CUSTOM_CONTENT))
		{
			// dont need set configurations for Images, Files content types
			return ctype;
		}
		// check if configured path to file-configuration
		if (ctypeXML.getCfgFile() != null && !ctypeXML.getCfgFile().isEmpty())
		{
			InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("test-data/contenttype/" + ctypeXML.getCfgFile());
			ctype.setConfiguration(readConfiguration(in));
		} else
		{

			ctype.setConfiguration(ctypeXML.getConfigdata());
		}
		return ctype;
	}

	/**
	 * Converts xml-data to {@link ContentRepository} instance.
	 * 
	 * @param cRepXML
	 *            {@link ContentRepositoryXml} instance.
	 * @return {@link ContentRepository} instance.
	 */
	public static ContentRepository convertXmlDataToContentRepository(ContentRepositoryXml cRepXML)
	{
		ContentRepository contentRepository = new ContentRepository();
		contentRepository.setName(cRepXML.getName());
		contentRepository.setDefaultLanguage(cRepXML.getDefaultLanguage());
		if(cRepXML.getTopCategory()!=null)
		{
			contentRepository.setTopCategory(convertXmlDataToTopCategory(cRepXML.getTopCategory()));
		}
		
		if(cRepXML.getCategories()!=null)
		{
			contentRepository.setCategories(convertListXmlCategories(cRepXML.getCategories()));
		}
		
		//contentRepository.setSelectedTypes(cRepXML.getAllowedContentTypes());

		return contentRepository;
	}
	
	public static ContentCategory convertXmlDataToContentCategory(ContentCategoryXml catXml)
	{
		ContentCategory cat = new ContentCategory();
		cat.setContentTypeName(catXml.getContentTypeName());
		cat.setName(catXml.getName());
		cat.setDescription(catXml.getDescription());
		cat.setParentName(catXml.getParentName());
		return cat;
	}
	public static List<ContentCategory>convertListXmlCategories(List<ContentCategoryXml> cats)
	{
		List<ContentCategory> list = new ArrayList<>();
		for(ContentCategoryXml categoryXml:cats)
		{
			list.add(convertXmlDataToContentCategory(categoryXml));
		}
		return list;
	}

	/**
	 * Converts xml-data to {@link TopCategory} instance.
	 * 
	 * @param topCat
	 *            {@link TopCategoryXml} instance.
	 * @return {@link TopCategory} instance.
	 */
	private static TopCategory convertXmlDataToTopCategory(TopCategoryXml topCatXml)
	{
		TopCategory topcategory = new TopCategory();
		topcategory.setName(topCatXml.getName());
		topcategory.setContentType(convertXmlDataToContentType(topCatXml.getContentType()));
		topcategory.setDescription(topCatXml.getDescription());
		return topcategory;
	}

	/**
	 * Reads data in XML for ContentType configuration Text Area.
	 * 
	 * @param in
	 *            {@link InputStream} instance.
	 * @return configuration as String.
	 */
	private static String readConfiguration(InputStream in)
	{
		StringBuilder sb = new StringBuilder();
		Scanner scanner = new Scanner(in);
		while (scanner.hasNextLine())
		{
			sb.append(scanner.nextLine());

		}
		return sb.toString();
	}
}
