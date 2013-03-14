package com.enonic.autotests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public class BaseTest {

	private String baseUrl;

	private WebDriver driver;

	private String driverName;

	@BeforeSuite(alwaysRun = true)
	@Parameters({ "browser", "base.url" })
	public void setupBeforeSuite(String browser, String url) {
		driverName = browser;
		baseUrl = url;
		// driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		// driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		// driver.manage().window().setSize(new Dimension(1920, 1080))

	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public String getBaseUrl() {
		return baseUrl;
	}
}
