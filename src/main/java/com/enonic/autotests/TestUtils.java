package com.enonic.autotests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.logger.Logger;

public class TestUtils {
	//public static Logger logger = Logger.getLogger();
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd-HH-mm-ss";

	private static TestUtils instance;

	public static final long TIMEOUT_IMPLICIT = 10;


	/**
	 * @return
	 */
	public static TestUtils getInstance() {
		if (instance == null) {
			synchronized (TestUtils.class) {
				if (instance == null) {
					instance = new TestUtils();
				}
			}
		}
		return instance;
	}

	/**
	 * The Default constructor.
	 */
	private TestUtils() {

	}

	public void waitUntilVisible(final TestSession testSession, final By by) {
		new WebDriverWait(testSession.getDriver(), TIMEOUT_IMPLICIT).until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public void waitUntilTitleVisible(final TestSession testSession, final String title) {
		(new WebDriverWait(testSession.getDriver(), TestUtils.TIMEOUT_IMPLICIT)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().trim().contains(title);
			}
		});
	}

	public boolean alertIsPresent(final TestSession testSession) {
		WebDriverWait wait = new WebDriverWait(testSession.getDriver(), 2);

		// return wait.until(ExpectedConditions.alertIsPresent())==null;
		try {

			wait.until(ExpectedConditions.alertIsPresent());
			System.out.println("alert was present");
			return true;
		} catch (Exception e) {
			System.out.println("alert was not present");
			return false;

		}

	}

	/**
	 * @param screenshotFileName
	 * @param driver
	 */
	public String saveScreenshot(final TestSession testSession) {
		WebDriver driver = testSession.getDriver();
		String fileName = timeNow() + ".png";
		File folder = new File(System.getProperty("user.dir") + File.separator + "snapshots");

		if (!folder.exists()) {
			if (!folder.mkdir()) {
				System.out.println("Folder for snapshots was not created ");
			} else {
				System.out.println("Folder for snapshots was created " + folder.getAbsolutePath());
			}
		}
		File screenshot = null;

		if ((Boolean) testSession.get(TestSession.IS_REMOTE)) {

			WebDriver augmentedDriver = new Augmenter().augment(driver);
			screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		} else {
			screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		}

		String fullFileName = folder.getAbsolutePath() + File.separator + fileName;

		try {
			FileUtils.copyFile(screenshot, new File(fullFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block

		}
		return fileName;
	}

	public String timeNow() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());

	}

	public void clickByLocator1(final By locator, WebDriver driver) {
		WebElement myDynamicElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(locator));
		myDynamicElement.click();
	}

	/**
	 * @param locator
	 * @param driver
	 */
	public void clickByLocator(final By locator, final WebDriver driver) {
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
		// TODO add perfomance log
		//logger.perfomance("clickByLocator:" + locator.toString(), startTime);
		// staticlogger.info("Finished click after waiting for " + totalTime +
		// " milliseconds.");
	}

	/**
	 * @param by
	 * @param driver
	 * @return
	 */
	public boolean checkIfDisplayed(final By by, final WebDriver driver) {
		List<WebElement> elements = driver.findElements(by);
		return ((elements.size() > 0) && (elements.get(0).isDisplayed()));
	}
//	public boolean checkIfDisplayed(final By by, final WebDriver driver) {
//		WebElement element = driver.findElement(by);
//		return element.isDisplayed();
//	}

	/**
	 * @param browser
	 * @return
	 */
	public void createDriverAndOpenBrowser(final TestSession testSession) throws IOException {
		WebDriver driver = null;

		String browserName = (String) testSession.get(TestSession.BROWSER_NAME);
		BrowserName cfgBrowser = BrowserName.findByValue(browserName);
		Boolean isRemote = (Boolean) testSession.get(TestSession.IS_REMOTE);

		if (isRemote != null && isRemote) {
			String hubUrl = (String) testSession.get(TestSession.HUB_URL);
			Capabilities desiredCapabilities = createDesiredCapabilities(testSession);
			driver = new RemoteWebDriver(new URL(hubUrl), desiredCapabilities);
			testSession.put(TestSession.WEBDRIVER, driver);
			return;
		}

		if (cfgBrowser == null) {
			throw new RuntimeException("browser was not specified in the suite file!");
		}
		if (cfgBrowser.equals(BrowserName.FIREFOX)) {
			// ProfilesIni allProfiles = new ProfilesIni();
			FirefoxProfile myProfile = new FirefoxProfile();
			myProfile.setPreference("capability.policy.default.Window.frameElement.get", "allAccess");
			driver = new FirefoxDriver(myProfile);
			driver.manage().window().maximize();
		} else if (cfgBrowser.equals(BrowserName.CHROME)) {
			driver = new ChromeDriver();
		} else if (cfgBrowser.equals(BrowserName.IE)) {
			driver = new InternetExplorerDriver();
		} else if (cfgBrowser.equals(BrowserName.HTMLUNIT)) {
			driver = new HtmlUnitDriver();
		} else {
			throw new RuntimeException("Driver not supported:" + cfgBrowser.getName());
		}

		testSession.put(TestSession.WEBDRIVER, driver);

	}

	private static Capabilities createDesiredCapabilities(final TestSession testSession) {
		String browserName = (String) testSession.get(TestSession.BROWSER_NAME);
		BrowserName cfgBrowser = BrowserName.findByValue(browserName);
		// Capabilities capability = null;
		DesiredCapabilities capability = null;

		if (cfgBrowser.equals(BrowserName.FIREFOX)) {

			capability = DesiredCapabilities.firefox();
			capability.setBrowserName("firefox");
			capability.setPlatform(Platform.WINDOWS);
			capability.setVersion("18.0");
			// capability = DesiredCapabilities.firefox();
			// capability.setBrowserName("firefox");
			// capability.setVersion("17.0.1");

			// TODO create for CHROME
		} else {
			capability = DesiredCapabilities.internetExplorer();
			capability.setPlatform(Platform.WINDOWS);
			capability.setVersion("19.0.2");
		}

		return capability;
	}

	public void selectByText(TestSession session, By by, String text) {
		Select select = new Select(session.getDriver().findElement(by));
		select.selectByVisibleText(text);
	}

	// driver.switch.frame(driver.findElementByXpath("//iframe[contains(@src,'forsee')]"))
	// xpath=id('backOptionsArea')//img[contains(@src,'color/02')]/../..
}
