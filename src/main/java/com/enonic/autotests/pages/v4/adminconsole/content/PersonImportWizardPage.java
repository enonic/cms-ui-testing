package com.enonic.autotests.pages.v4.adminconsole.content;

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
import com.enonic.autotests.exceptions.ImportContentException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Person import' wizard.
 * 
 * <br>
 * This page available if "Person" content type was created!
 * 
 */
public class PersonImportWizardPage extends AbstractAdminConsolePage
{

	@FindBy(name = "importfile")
	private WebElement chooseFileInputType;

	@FindBy(name = "importbtn")
	private WebElement importButton;

	private String SELECT_IMPORT_NAME = "//select[@name='importname']";

	/**
	 * @param session
	 */
	public PersonImportWizardPage( TestSession session )
	{
		super(session);

	}

	/**
	 * Imports file, that located in folder 'resources'.
	 * 
	 * Importing of content performs in the following way:
	 * 
	 * 1. Chooses a file and clicks by the button "Import". When the import is successful, button "Back" appears. <br>
	 * 2. Click by button "Back" and shows a table of content
	 * 
	 * @param importName
	 * @param fileName
	 */
	public void doImportFromFile(String importName, String fileName)
	{
		//1. select the name of import.
		if (importName != null)
		{
			// TODO verify is present in select!
			TestUtils.getInstance().selectByText(getSession(), By.xpath(SELECT_IMPORT_NAME), importName);
		}
		
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
			chooseFileInputType.sendKeys(file.getAbsolutePath());
		} else
		{  // use the RemoteWebElement and  LocalFileDetector!!!
			LocalFileDetector detector = new LocalFileDetector();
			WebElement fileInputType = findElement(By.id("importfile"));
			File localFile = detector.getLocalFile(file.getAbsolutePath());
			((RemoteWebElement) fileInputType).setFileDetector(detector);

			fileInputType.sendKeys(localFile.getAbsolutePath());
		}
        // 3. click by the "Import" button
		importButton.click();
		
		//4. wait until button "Back" appears and press this button
		boolean isBackButtonPresent = TestUtils.getInstance().waitAndFind(By.xpath("//button[text()='Back']"), getDriver());
		if (!isBackButtonPresent)
		{
			throw new ImportContentException("Error during content importing, 'Back' button was not found! ");
		}
		findElement(By.xpath("//button[text()='Back']")).click();

	}

	/**
	 * Import file from system-tmp folder
	 * @param importName
	 * @param file
	 */
	public void doImportFromTmpFile(String importName, File tmpfile)
	{
		//1. select the name of import.
		if (importName != null)
		{
			// TODO verify is present in select!
			TestUtils.getInstance().selectByText(getSession(), By.xpath(SELECT_IMPORT_NAME), importName);
		}
				
		//2. choose a file:
		if (!getSession().getIsRemote())
		{
			// use the a proxy and absolute path to file:
			chooseFileInputType.sendKeys(tmpfile.getAbsolutePath());
		} else
		{  // use the RemoteWebElement and  LocalFileDetector!!!
			LocalFileDetector detector = new LocalFileDetector();
			WebElement fileInputType = findElement(By.id("importfile"));
			File localFile = detector.getLocalFile(tmpfile.getAbsolutePath());
			((RemoteWebElement) fileInputType).setFileDetector(detector);

			fileInputType.sendKeys(localFile.getAbsolutePath());
		}
        // 3. click by the "Import" button
		importButton.click();
		
		//4. wait until button "Back" appears and press this button
		boolean isBackButtonPresent = TestUtils.getInstance().waitAndFind(By.xpath("//button[text()='Back']"), getDriver());
		if (!isBackButtonPresent)
		{
			throw new ImportContentException("Error during content importing, 'Back' button was not found! ");
		}
		findElement(By.xpath("//button[text()='Back']")).click();

	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SELECT_IMPORT_NAME)));
	}

}
