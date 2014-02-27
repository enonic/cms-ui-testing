package com.enonic.autotests.pages.adminconsole.content;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;

public class ZipImportWizardPage  extends AbstractAdminConsolePage
{
	@FindBy(name = "zipfile")
	private WebElement chooseZipInputType;
	
	@FindBy(xpath = "//button[text()='Next']")
	private WebElement nextButton;
	
	private String IMPORT_BUTTON_XPATH = "//button[text()='Import']";


	public ZipImportWizardPage( TestSession session )
	{
		super(session);
		
	}

	public void doImportZipFile(String fileName,boolean publish)
	{
		URL dirURL = ContentConvertor.class.getClassLoader().getResource(fileName);
		File file = null;
		try
		{
			file = new File(dirURL.toURI());
		} catch (URISyntaxException e)
		{
			getLogger().error("Error during importing a content: Wrong file URL ", getSession());

		}
		//2. choose a file:
		if (!getSession().getIsRemote())
		{
			// use the a proxy and absolute path to file:
			chooseZipInputType.sendKeys(file.getAbsolutePath());
		} else
		{ 
			LocalFileDetector detector = new LocalFileDetector();
			WebElement fileInputType = findElement(By.id("zipfile"));
			File localFile = detector.getLocalFile(file.getAbsolutePath());
			((RemoteWebElement) fileInputType).setFileDetector(detector);

			fileInputType.sendKeys(localFile.getAbsolutePath());
		}
		
		//click by 'next'
		nextButton.click();
		TestUtils.getInstance().waitUntilInvisibleNoException(getSession(), By.xpath(IMPORT_BUTTON_XPATH), 1);
		getDriver().findElement(By.xpath(IMPORT_BUTTON_XPATH)).click();
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name = 'zipfile']")));
		
	}
}
