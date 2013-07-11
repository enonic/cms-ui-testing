package com.enonic.autotests.testdata.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.enonic.autotests.testdata.contenttype.ContentTypeXml;

public class TT1
{
	public static void main0(String[] args) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(ContentRepositoryTestData.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		ContentRepositoryTestData list = new ContentRepositoryTestData();
		ContentRepositoryXml ct =  new ContentRepositoryXml();
		ct.setName("name1");
		ct.setDefaultLanguage("English");


		TopCategoryXml topCategory = new TopCategoryXml();
		topCategory.setName("top1");
		topCategory.setDescription("descr");
		ContentTypeXml contType = new ContentTypeXml();
		contType.setName("images");
		contType.setContentHandler("Images");
		topCategory.setContentType(contType);
		ct.setTopCategory(topCategory );
		ct.setName("nnname");
		
		list.getContentRepositories().add(ct);
		File  data = new File("d:\\uu.xml");
		OutputStream os =new FileOutputStream(data);
		marshaller.marshal(list, os);

	}

	public static void main(String[] args) throws JAXBException, FileNotFoundException
	{
		JAXBContext context = JAXBContext.newInstance(ContentRepositoryTestData.class);
		 Marshaller marshaller = context.createMarshaller();
		 marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
		 ContentRepositoryTestData list = new ContentRepositoryTestData();
		// List<ContentRepositoryXml> repos =  new ArrayList<>();
		 ContentRepositoryXml ct = new ContentRepositoryXml();
		 ct.setCaseInfo("caseinfo");
		 ct.setDefaultLanguage("English");
		 ct.setName("repo1");
		 
		 List<ContentCategoryXml> catgories = new ArrayList<>();
		 ContentCategoryXml cat = new ContentCategoryXml();
		 cat.setContentTypeName("ctypename");
		 cat.setName("name");
		 cat.setParentName("parent");
		 catgories.add(cat);
		 //catgories.add("cat2");
		
		 
		 ct.setCategories(catgories );
		 
		 TopCategoryXml topCategory = new TopCategoryXml();
		 ContentTypeXml contentType = new ContentTypeXml();
		 contentType.setContentHandler("Files");
		 topCategory.setContentType(contentType );
		 
		 ct.setTopCategory(topCategory );
		// repos.add(ct);
		 list.getContentRepositories().add(ct);
		 File file = new File("d:\\zzz.xml");
		 OutputStream os =new FileOutputStream(file);
		 marshaller.marshal(list, os);
		 
//			
//			Unmarshaller unmarshaller = context.createUnmarshaller();
//			File data = new File("d:\\jaxb\\crepo-test-data.xml");
//			InputStream in = new FileInputStream(data);
//	
//			ContentRepositoryTestData testdata = (ContentRepositoryTestData) unmarshaller.unmarshal(in);
//			List<ContentRepositoryXml> list = testdata.getContentRepositories();
//			list.get(0);
	}
	public static void mainz(String[] args) throws JAXBException, FileNotFoundException
	{
		 JAXBContext context = JAXBContext.newInstance(ContentTestData.class);
		 Marshaller marshaller = context.createMarshaller();
		 marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
		 Boolean.TRUE);
		
		 ContentTestData res = new ContentTestData();
		 List<AbstractContentXml> contentList = new ArrayList<>();
		 ImageContentXml image = new ImageContentXml();
		 image.setDisplayName("displ");
		 image.setContentHandler("Images");
		 image.setCopyright("copyright");
		 image.setPathToFile("d:/bel.gif");
		 image.setComments("comments");
		 image.setCaseInfo("add image");
		 image.setPhotographerEmail("aa@gmail.com");
		 image.setPhotographerName("Joan");
		 image.setCategoryName("categoryName");
		 contentList.add(image);
		 FileContentXml file = new FileContentXml();
		 file.setDisplayName("displf");
		 file.setContentHandler("Files");
		 file.setPathToFile("d:/bel.gif");
		 contentList.add(file);
		
		
		 res.setContentList(contentList);
		
		 File data = new File("d:\\qqq.xml");
		 OutputStream os =new FileOutputStream(data);
		 marshaller.marshal(res, os);
		 
		
//			System.out.println("oo");
		//
//		// //
//		JAXBContext context = JAXBContext.newInstance(ContentTestData.class);
//		Unmarshaller unmarshaller = context.createUnmarshaller();
//		File data = new File("d:\\qqq.xml");
//		InputStream in = new FileInputStream(data);
//
//		ContentTestData testdata = (ContentTestData) unmarshaller.unmarshal(in);
//		List<AbstractContentXml> list = testdata.getContentList();
//		list.get(0);
//		System.out.println("oo");
	}
}
