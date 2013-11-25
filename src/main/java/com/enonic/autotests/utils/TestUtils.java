package com.enonic.autotests.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentRepositoryException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.PageNavigator;

public class TestUtils
{
	public static Logger logger = Logger.getLogger();
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd-HH-mm-ss";

	private static TestUtils instance;

	public static final long TIMEOUT_IMPLICIT = 4;

	/**
	 * @return
	 */
	public static TestUtils getInstance()
	{
		if (instance == null)
		{
			synchronized (TestUtils.class)
			{
				
				instance = new TestUtils();
			}
		}
		return instance;
	}

	/**
	 * The Default constructor.
	 */
	private TestUtils()
	{

	}
	
	
	/**
	 * Finds option from the select and performs double click action.
	 * 
	 * @param testSession
	 * @param allOptions
	 * @param optionText
	 */
	public void doubleClickActionByOption(final TestSession testSession, List<WebElement> allOptions , String optionText) 
	{	
		boolean isFound = false;
		for (WebElement option : allOptions) {
			logger.debug(String.format("option was found : %s", option.getText()));
			if (option.getText().equals(optionText)) {
				Actions builder = new Actions(testSession.getDriver());
				//builder.doubleClick(option).perform();//dosent work
				//builder.doubleClick(option).build().perform();
				builder.moveToElement(option).click().build().perform();
				//Actions builder2 = new Actions(testSession.getDriver());//builder.moveToElement(option).clickAndHold().release().build().perform();
				builder.moveToElement(option).doubleClick().build().perform();//builder.moveToElement(option).perform();option
				//Actions builder3 = new Actions(testSession.getDriver());
				//builder3.moveToElement(testSession.getDriver().findElement(By.xpath("//select[@name='availablect']"))).perform();
				//option.click();
				//boolean aa = (((RemoteWebElement)option).findElements(By.xpath("//option[text()='File']")).get(0)).isSelected();
				//Actions builder2 = new Actions(testSession.getDriver());
				//builder2.moveToElement(testSession.getDriver().findElements(By.xpath("//option[text()='File']")).get(0)).click().perform();
				//builder2.moveToElement(testSession.getDriver().findElements(By.xpath("//option[text()='File']")).get(0)).doubleClick().perform();
				isFound = true;//builder.moveToElement(((RemoteWebElement)option).findElements(By.xpath("//option[text()='File']")).get(0)).is
				break;
			}

		}
		if (!isFound) {
			throw new ContentRepositoryException("The " + optionText + " content type was not found among available content types        ");
		}

	}

	/**
	 * Types text in input field.
	 * 
	 * @param session
	 * @param input input type=text
	 * @param text text for input.
	 */
	public void clearAndType(TestSession session, WebElement input, String text)
	{
		if(session.getIsRemote())
		{
			input.sendKeys(Keys.chord(Keys.CONTROL, "a"), text);
		} 
		else{
			
			String os = System.getProperty("os.name").toLowerCase();
			logger.info("clearAndType: OS System is " + os);
			if (os.indexOf("mac") >= 0)
			{
				input.sendKeys(Keys.chord(Keys.COMMAND, "a"), text);
			}else{
				input.sendKeys(Keys.chord(Keys.CONTROL, "a"), text);
			}
			
		}
		

	}

	/**
	 * @param testSession
	 * @param by
	 * @param timeout
	 */
	public void waitUntilVisible(final TestSession testSession, final By by, long timeout)
	{
		new WebDriverWait(testSession.getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	/**
	 * @param testSession
	 * @param title
	 */
	public void waitUntilTitleVisible(final TestSession testSession, final String title)
	{
		(new WebDriverWait(testSession.getDriver(), TestUtils.TIMEOUT_IMPLICIT)).until(new ExpectedCondition<Boolean>()
		{
			public Boolean apply(WebDriver d)
			{
				return d.getTitle().trim().contains(title);
			}
		});
	}

	/**
	 * @param testSession
	 * @param by
	 * @param timeout
	 * @return
	 */
	public boolean waitUntilVisibleNoException(final TestSession testSession, By by, long timeout)
	{
		WebDriverWait wait = new WebDriverWait(testSession.getDriver(), timeout);
		try
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			return true;
		} catch (Exception e)
		{

			return false;
		}
	}

	public boolean waitUntilClickableNoException(final TestSession testSession, By by, long timeout)
	{
		WebDriverWait wait = new WebDriverWait(testSession.getDriver(), timeout);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(by));
			return true;
		} catch (Exception e)
		{

			return false;
		}
	}
	public boolean waitUntilInvisibleNoException(final TestSession testSession, By by, long timeout)
	{
		WebDriverWait wait = new WebDriverWait(testSession.getDriver(), timeout);
		try
		{
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
			return true;
		} catch (Exception e)
		{

			return false;
		}
	}

	
	/**
	 * Checks if alert dialog present.
	 * @param testSession
	 * @param timeout
	 * @return
	 */
	public boolean alertIsPresent(final TestSession testSession, long timeout)
	{
		WebDriverWait wait = new WebDriverWait(testSession.getDriver(), timeout);
		try
		{
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (Exception e)
		{

			return false;

		}

	}

	public void scrollWindow(final TestSession testSession, int xnumpixels, int ynumpixels )
	{
		String xp = Integer.toString(xnumpixels);
		String yp = Integer.toString(xnumpixels);
		String script = String.format("window.scrollBy(%s,%s)", xp,yp);
		((JavascriptExecutor) testSession.getDriver()).executeScript(script);
			
	}

	/**
	 * @param screenshotFileName
	 * @param driver
	 */
	public String saveScreenshot(final TestSession testSession)
	{
		WebDriver driver = testSession.getDriver();
		String fileName = timeNow() + ".png";
		File folder = new File(System.getProperty("user.dir") + File.separator + "snapshots");

		if (!folder.exists())
		{
			if (!folder.mkdir())
			{
				System.out.println("Folder for snapshots was not created ");
			} else
			{
				System.out.println("Folder for snapshots was created " + folder.getAbsolutePath());
			}
		}
		File screenshot = null;

		if ((Boolean) testSession.get(TestSession.IS_REMOTE))
		{

			WebDriver augmentedDriver = new Augmenter().augment(driver);
			screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		} else
		{
			screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		}

		String fullFileName = folder.getAbsolutePath() + File.separator + fileName;

		try
		{
			FileUtils.copyFile(screenshot, new File(fullFileName));
		} catch (IOException e)
		{

		}
		return fileName;
	}

	public String timeNow()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());

	}

	public void clickByLocator1(final By locator, WebDriver driver)
	{
		WebElement myDynamicElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(locator));
		myDynamicElement.click();
	}

	/**
	 * @param locator
	 * @param driver
	 */
	public void clickByLocator(final By locator, final WebDriver driver)
	{
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(90000, TimeUnit.MILLISECONDS).pollingEvery(5500, TimeUnit.MILLISECONDS);
		wait.until(new ExpectedCondition<Boolean>()
		{
			@Override
			public Boolean apply(WebDriver webDriver)
			{
				try
				{
					webDriver.findElement(locator).click();
					return true;
				} catch (StaleElementReferenceException e)
				{
					return false;
				}
			}
		});
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	/**
	 * @param by
	 * @param driver
	 * @return
	 */
	public boolean waitAndFind(final By by, final WebDriver driver)
	{

		return waitAndFind(by, driver, AppConstants.PAGELOAD_TIMEOUT);
	}
	
	public boolean waitAndFind(final By by, final WebDriver driver, long timeout )
	{

		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		List<WebElement> elements = driver.findElements(by);
		return ((elements.size() > 0) && (elements.get(0).isDisplayed()));
	}

	public boolean waitIsDispalyedElement(WebElement elem, final WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 2l, 500);
		try
		{
			wait.until(ExpectedConditions.visibilityOf(elem));
			return true;
		} catch (Exception e)
		{

			return false;
		}
	}

	/**
	 * Refresh Left Frame Menu.
	 * 
	 * @param driver
	 *            {@link WebDriver} instance.
	 * 
	 */
	public static void refreshLeftFrame(final WebDriver driver)
	{
		List<WebElement> elements = driver.findElements(By.xpath(AbstractAdminConsolePage.REFRESH_IMAGE_XPATH));
		if (elements.size() > 0)
		{
			elements.get(0).click();
		} else
		{
			throw new TestFrameworkException("Refresh left Frame Action was failed");
		}
	}

	/**
	 * @param session
	 * @param by
	 * @param text
	 */
	public void selectByText(TestSession session, By by, String text)
	{
		List<WebElement> selectElements = session.getDriver().findElements(by);
		if (selectElements.size() == 0)
		{
			throw new TestFrameworkException("wrong xpath for select: " + text);
		}
		Select select = new Select(selectElements.get(0));
		boolean result = verifyTextInSelect(select, text);
		if(!result)
		{
			throw new TestFrameworkException("option was not found in the select "+ text);
		}
		select.selectByVisibleText(text);
	}
	
	/**
	 * @param select
	 * @param text
	 * @return
	 */
	private boolean verifyTextInSelect(Select select,String text)
	{
		 List<WebElement> options = select.getOptions();
		 for(WebElement opt: options)
		 {
			 if(opt.getText().contains(text))
			 {
				 return true;
			 }
			
		 }
		 return false;
	}
	public boolean expandFolder(TestSession session,String expanderXpath)
	{
		PageNavigator.switchToFrame( session, AbstractAdminConsolePage.LEFT_FRAME_NAME );
		List<WebElement> expanderElems = session.getDriver().findElements(By.xpath(expanderXpath));
		
		if (expanderElems.size() == 0)
		{
			logger.info("expandFolder:this folder has no one item!"+ expanderXpath);
			return false;
			
		}
		// check if category has + expander:
		WebElement img = expanderElems.get(0);
		String attribute = img.getAttribute("src");
		if(attribute == null)
		{
			Assert.fail("Element does not contain attribut 'src', probably wrong xpath");
		}
		if (attribute.contains(AppConstants.PLUS_ICON_PNG))
		{
			expanderElems.get(0).click();
			
		} else if (img.getAttribute("src").contains(AppConstants.MINUS_ICON_PNG))
		{
			logger.info("the folder with name  already expanded");
		}
		return true;
	}

	public String createTempFile(String s)
	{
		try
		{
			File f = File.createTempFile("uploadTest", "tempfile");
			f.deleteOnExit();
			writeStringToFile(s, f);
			return f.getAbsolutePath();
		} catch (Exception e)
		{
			throw new TestFrameworkException("Error during creation TMP-file");

		}
	}

	public void writeStringToFile(String s, File file) throws IOException
	{
		FileOutputStream in = null;
		try
		{
			in = new FileOutputStream(file);
			FileChannel fchan = in.getChannel();
			BufferedWriter bf = new BufferedWriter(Channels.newWriter(fchan, "UTF-8"));
			bf.write(s);
			bf.close();
		} finally
		{
			if (in != null)
			{
				in.close();
			}
		}
	}
	
	public String getAlertMessage(TestSession session)
	{
		Alert alert = session.getDriver().switchTo().alert();
		String msg = alert.getText();
		alert.dismiss();
		return msg;
	}
	/**
	 * Reads data in XML for ContentType configuration Text Area.
	 * 
	 * @param in
	 *            {@link InputStream} instance.
	 * @return configuration as String.
	 */
	public  String readConfiguration(InputStream in)
	{
		StringBuilder sb = new StringBuilder();
		Scanner scanner = new Scanner(in);
		while (scanner.hasNextLine())
		{
			sb.append(scanner.nextLine());

		}
		scanner.close();
		return sb.toString();
		
	}

}
