package com.enonic.autotests;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestUtils {


	public  static void saveScreenshot(String screenshotFileName,WebDriver driver)  {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenshot, new File(screenshotFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}
	}
	
	public static boolean checkIfDisplayed(By by,WebDriver driver)
	{
	   List<WebElement> elements = driver.findElements(by);
	   return ((elements.size() > 0) && (elements.get(0).isDisplayed()));
	}
}
