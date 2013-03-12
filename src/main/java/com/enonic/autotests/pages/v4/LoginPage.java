package com.enonic.autotests.pages.v4;



import org.openqa.selenium.By;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestUtils;
import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.pages.Page;

public class LoginPage extends Page {
	
	private Logger logger = Logger.getLogger(LoginPage.class);
	//TODO message should be localized:
	private String errorMessage = "Wrong username or password";

	private String title = "Enonic CMS - Login";

	@FindBy(how = How.NAME, using = "username")
	private WebElement usernameInput;
	@FindBy(how = How.NAME, using = "password")
	private WebElement passwordInput;
	@FindBy(how = How.NAME, using = "login")
	private WebElement loginButton;


	public LoginPage(WebDriver driver) {
		setDriver(driver);
		(new WebDriverWait(getDriver(), TIMEOUT)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().trim().contains(title);
            }
        });
		PageFactory.initElements(driver, this);
	}

	public void doLogin(String username, String password) {

		usernameInput.sendKeys(username);
		passwordInput.sendKeys(password);
		TestUtils.saveScreenshot("credentials.png", getDriver());
		loginButton.submit();
		if(TestUtils.checkIfDisplayed(By.className("cms-error"), getDriver())){
			String erMess = getDriver().findElement(By.className("cms-error")).getText();
			logger.info("could not to login "+erMess);
			TestUtils.saveScreenshot("error.png", getDriver());
			throw new AuthenticationException("Wrong username or password");
		}

	}

	@Override
	public String getTitle() {
		return title;
	}
}
