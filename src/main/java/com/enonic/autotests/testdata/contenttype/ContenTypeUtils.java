package com.enonic.autotests.testdata.contenttype;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.ContentType;

public class ContenTypeUtils {
 public static String BASE_PATH_TO_RES = "src"+File.separator+"test"+File.separator+"resources"+File.separator+"contenttype"+File.separator;
 //public static String BASE_PATH_TO_RES = "src"+File.separator+"test"+File.separator+"resources"+File.separator;
	/**
	 * Converts from {@link ContentTypeXml} to {@link ContentType}.
	 * 
	 * @param ctypeXML
	 * @return {@link ContentType} instance.
	 */
	public static ContentType convertToModel(ContentTypeXml ctypeXML) {
		ContentType ctype = new ContentType();
		ctype.setName(ctypeXML.getName());
		ctype.setDescription(ctypeXML.getDescription());
		ctype.setPathToCSS(ctypeXML.getPathToCSS());
		ctype.setContentHandler(ctypeXML.getContentHandler());
		if(ctypeXML.getCfgFile()!=null && !ctypeXML.getCfgFile().isEmpty()){
			ctype.setConfiguration(readConfiguration(BASE_PATH_TO_RES+ctypeXML.getCfgFile()));
		}
		return ctype;
	}
	
	private static String readConfiguration(String fileName){
		File confXML = new File(fileName);
		StringBuilder sb = new StringBuilder();
	    Scanner scanner;
		try {
			scanner = new Scanner(confXML);
		
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
           
        }
		} catch (FileNotFoundException e) {
			throw new TestFrameworkException("error during reading the test-data..."+ e.getMessage());
		}
        return sb.toString();
	}
}
