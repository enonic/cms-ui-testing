package com.enonic.autotests.contentimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Scanner;

public class TT
{
public static void main(String[] args) throws FileNotFoundException
{
	StringBuilder sb = readAndChangeResource("556");
//	String xx = "<person id=\"1002\" face-key=\"203\"/>";
//	//Scanner sc = new Scanner("d:/p.xml");
//	StringBuilder sb = new StringBuilder();
//	InputStream in = new  FileInputStream(new File("d:/p.xml"));
//	Scanner scanner = new Scanner(in);
//	while (scanner.hasNextLine())
//	{
//		String ss = scanner.nextLine();
//		
//		if(ss.contains("face-key"))
//		{
//			//StringBuilder sb2  = new StringBuilder();
//			//sb2.append(ss);
//			
//			int startindex  = xx.indexOf("face-key=");
//			
//			String res = ss.substring(0,startindex+12)+"707"+ "\">";
//			
//			sb.append(res);
//		}else{
//			sb.append(ss);
//		}
//		
//
//	}
//	scanner.close();
	System.out.println(sb.toString());

	
}
private static StringBuilder readAndChangeResource(String imageKey) throws FileNotFoundException
{
	//InputStream in = this.getClass().getClassLoader().getResourceAsStream(PERSONS_WITH_IMAGE_TO_IMPORT);
	InputStream in = new FileInputStream(new File("d:/p.xml"));
	StringBuilder sb = new StringBuilder();
	Scanner scanner = new Scanner(in);
	while (scanner.hasNextLine())
	{
		String line = scanner.nextLine();
		if (line.contains("face-key"))
		{
			int startindex = line.indexOf("face-key=");
			int ss = "face-key=".length();
			String res = line.substring(0, startindex +ss)  + "\"" + imageKey + "\">";

			sb.append(res);
		}else{
			sb.append(line);
		}
		

	}
	scanner.close();
	return sb;
}
}
