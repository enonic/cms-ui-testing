package com.enonic.autotests.testdata.contenttype;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import com.enonic.autotests.model.ContentType;

public class ContenTypeUtils {
 
	/**
	 * Converts from {@link ContentTypeXml} to {@link ContentType}.
	 * 
	 * @param ctypeXML
	 * @return {@link ContentType} instance.
	 */
	public static ContentType convertToModel(ContentTypeXml ctypeXML) {
		InputStream in = ContenTypeUtils.class.getClassLoader().getResourceAsStream("contenttype/"+ ctypeXML.getCfgFile());
		String fs  =System.getProperty("file.separator");

		//InputStream in = ContenTypeUtils.class.getClassLoader().getResourceAsStream("contenttype"+fs+ ctypeXML.getCfgFile());
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
