package com.enonic.autotests;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class TestUtils {
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd-HH-mm-ss";

	/**
	 * @param screenshotFileName
	 * @param driver
	 */
	public static void saveScreenshot( WebDriver driver) {
		String fileName = timeNow()+".png";
		File folder = new File(System.getProperty("user.dir")+File.separator+"snapshots");
		
		if (!folder.exists()){
			if (!folder.mkdir()){
				System.out.println("Folder for snapshots was not created ");
			}else{
				System.out.println("Folder for snapshots was created "+ folder.getAbsolutePath());
			}
		}
				
		String fullFileName = folder.getAbsolutePath()+File.separator+fileName;
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenshot, new File(fullFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block

		}
	}
	public static String timeNow() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    return sdf.format(cal.getTime());

	  }
	
	/**
	 * @param locator
	 * @param driver
	 */
	public static void clickByLocator(final By locator, WebDriver driver) {
		// staticlogger.info( "Click by locator: " + locator.toString() );
		final long startTime = System.currentTimeMillis();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(90000, TimeUnit.MILLISECONDS).pollingEvery(5500,
				TimeUnit.MILLISECONDS);
		// .ignoring( StaleElementReferenceException.class );
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver webDriver) {
				try {
					webDriver.findElement(locator).click();
					return true;
				} catch (StaleElementReferenceException e) {
					// staticlogger.info( e.getMessage() + "\n");
					// staticlogger.info("Trying again...");
					return false;
				}
			}
		});
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		// staticlogger.info("Finished click after waiting for " + totalTime +
		// " milliseconds.");
	}

	/**
	 * @param by
	 * @param driver
	 * @return
	 */
	public static boolean checkIfDisplayed(By by, WebDriver driver) {
		List<WebElement> elements = driver.findElements(by);
		return ((elements.size() > 0) && (elements.get(0).isDisplayed()));
	}

	/**
	 * @param browser
	 * @return
	 */
	public static WebDriver createWebDriver(String browser) {
		BrowserName cfgBrowser = BrowserName.findByValue(browser);
		WebDriver driver = null;
		if (cfgBrowser == null) {
			throw new RuntimeException("browser was not specified in the suite file!");
		}
		if (cfgBrowser.equals(BrowserName.FIREFOX)) {
			driver = new FirefoxDriver();
		} else if (cfgBrowser.equals(BrowserName.CHROME)) {
			driver = new ChromeDriver();
		} else if (cfgBrowser.equals(BrowserName.IE)) {
			return new InternetExplorerDriver();
		} else if (cfgBrowser.equals(BrowserName.HTMLUNIT)) {
			driver = new HtmlUnitDriver();
		}
		return driver;
	}
}
