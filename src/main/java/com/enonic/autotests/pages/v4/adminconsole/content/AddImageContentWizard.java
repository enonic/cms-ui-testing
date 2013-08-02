package com.enonic.autotests.pages.v4.adminconsole.content;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.IContentInfo;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Add image content Wizard'
 *
 */
public class AddImageContentWizard extends AbstractAddContentWizard implements IUpdateOrCreateContent<ImageContentInfo>
{

	@FindBy(how = How.ID, using = "photographername")
	protected WebElement photographername;

	@FindBy(how = How.ID, using = "photographeremail")
	protected WebElement photographeremail;

	/**
	 * Constructor
	 * 
	 * @param session
	 */
	public AddImageContentWizard( TestSession session )
	{
		super(session);
	}

	@Override
	public void typeDataAndSave(Content<ImageContentInfo> newcontent)
	{
		IContentInfo<ImageContentInfo> contentTab = newcontent.getContentTab();
		String comment = contentTab.getContentTabInfo().getComment();
		//1. fill a comments field
		if (comment != null && !commentInput.getAttribute("value").equals(comment))
		{
		commentInput.sendKeys(contentTab.getContentTabInfo().getComment());
		}
		String description = contentTab.getContentTabInfo().getDescription();
		//2. fill a description field
		if (description != null && !descriptionTextarea.getAttribute("value").equals(description))
		{
		descriptionTextarea.sendKeys(contentTab.getContentTabInfo().getDescription());
		}
                //3. fill a photographername field.
		String phName = contentTab.getContentTabInfo().getPhotographerName();
		if (phName != null && !photographername.getAttribute("value").equals(phName))
		{
			photographername.sendKeys(newcontent.getContentTab().getContentTabInfo().getPhotographerName());
		}
		//4. fill a photographeremail field.
		String phEmail = contentTab.getContentTabInfo().getPhotographerEmail();
		if (!photographeremail.getAttribute("value").equals(phEmail))
		{
			photographeremail.sendKeys(newcontent.getContentTab().getContentTabInfo().getPhotographerEmail());
		}
	
		//5. specify a path to the file: 
		File file = new File(newcontent.getContentTab().getContentTabInfo().getPathToFile());
		String pathTofile = file.getAbsolutePath();

		if (!getSession().getDriver().findElement(By.id("origimagefilename")).getText().equals(pathTofile))
		{
		getSession().getDriver().findElement(By.id("origimagefilename")).sendKeys(pathTofile);
		}

		//6.  fill the display name:
		if (!nameInput.getAttribute("value").equals(newcontent.getDisplayName()))
		{
			TestUtils.getInstance().clearAndType(getSession(), nameInput, newcontent.getDisplayName());

		}
		saveButton.click();
		waituntilPageLoaded(4l);
		closeButton.click();		
	}
}
