package com.enonic.autotests.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentRepositoryException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.PageNavigatorV4;

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
				if (instance == null)
				{
					instance = new TestUtils();
				}
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
	
	public void doubleClickActionByOption(final TestSession testSession, List<WebElement> allOptions , String optionText) {
		
		boolean isFound = false;
		for (WebElement option : allOptions) {
			logger.debug(String.format("option was found : %s", option.getText()));
			if (option.getText().equals(optionText)) {
				Actions builder = new Actions(testSession.getDriver());
				//builder.doubleClick(option).perform();
				//builder.doubleClick(option).build().perform();
				builder.moveToElement(option).click().perform();
				builder.moveToElement(option).doubleClick().perform();
				
				isFound = true;
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
		String os = System.getProperty("os.name").toLowerCase();
		logger.info("clearAndType: OS System is " + os);
		input.sendKeys(Keys.chord(Keys.CONTROL, "a"), text);

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
			// TODO Auto-generated catch block

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
		final long startTime = System.currentTimeMillis();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(90000, TimeUnit.MILLISECONDS).pollingEvery(5500, TimeUnit.MILLISECONDS);
		// .ignoring( StaleElementReferenceException.class );
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
					// staticlogger.info( e.getMessage() + "\n");
					// staticlogger.info("Trying again...");
					return false;
				}
			}
		});
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// TODO add perfomance log
		// logger.perfomance("clickByLocator:" + locator.toString(), startTime);
		// staticlogger.info("Finished click after waiting for " + totalTime +
		// " milliseconds.");
	}

	/**
	 * @param by
	 * @param driver
	 * @return
	 */
	public boolean waitAndFind(final By by, final WebDriver driver)
	{

		driver.manage().timeouts().implicitlyWait(AppConstants.IMPLICITLY_WAIT, TimeUnit.SECONDS);
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

	public void selectByText(TestSession session, By by, String text)
	{
		List<WebElement> elems = session.getDriver().findElements(by);
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("wrong xpath for select: " + text);
		}
		Select select = new Select(elems.get(0));
		select.selectByVisibleText(text);
	}
	
	public void expandFolder(TestSession session,String expanderXpath)
	{
		PageNavigatorV4.switchToFrame(session, AbstractAdminConsolePage.LEFT_FRAME_NAME);
		List<WebElement> elems = session.getDriver().findElements(By.xpath(expanderXpath));
		
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("xpath for Expander  is wrong or this folder has no one item!");
		}
		// check if category has + expander:
		WebElement img = elems.get(0);
		if (img.getAttribute("src").contains(AppConstants.PLUS_ICON_PNG))
		{
			elems.get(0).click();
		} else if (img.getAttribute("src").contains(AppConstants.MINUS_ICON_PNG))
		{
			logger.info("the folder with name  already expanded");
		}
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

}
