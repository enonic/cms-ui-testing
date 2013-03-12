package com.enonic.autotests.pages.v4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestUtils;
import com.enonic.autotests.pages.Page;

public class HomePage extends Page {

	private String title = "Enonic CMS - Boot Page";

	@FindBy(xpath = "//span[text()='Admin Console']")
	private WebElement admConsoleLink;

	private String url;

	private boolean isLogged;

	public void open() {
		getDriver().get(url);
		if (!getDriver().getTitle().trim().equals(title)) {

			throw new IllegalStateException("This is not the login page");
		}
		(new WebDriverWait(getDriver(), TIMEOUT)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().trim().contains(title);
			}
		});
	}

	public HomePage(WebDriver driver, String homeUrl) {
		setDriver(driver);
		this.url = homeUrl;
		PageFactory.initElements(driver, this);
	}

	@Override
	public String getTitle() {

		return title;
	}

	public AdminConsoolePage openAdminConsole(String username, String password) {
		admConsoleLink.click();
		if (!isLogged) {
			LoginPage loginPage = new LoginPage(getDriver());
			loginPage.doLogin(username, password);
			new WebDriverWait(getDriver(), TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(By.className("leftframe")));
			isLogged = true;
		}
		TestUtils.saveScreenshot("adminconsole.png", getDriver());
		return new AdminConsoolePage(getDriver());
	}

	public boolean isLogged() {
		return isLogged;
	}

	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}
}
