package com.enonic.autotests.pages.v4.adminconsole.content;

import java.io.File;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentInfo;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Add image content Wizard'
 *
 */
public class AddImageContentWizard extends AbstractAddContentWizard implements IAddContentToRepository<ImageContentInfo>
{

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
		ContentInfo<ImageContentInfo> contentTab = newcontent.getContentTab();
		
		commentInput.sendKeys(contentTab.getContentTabInfo().getComment());
		descriptionTextarea.sendKeys(contentTab.getContentTabInfo().getDescription());
	
		File file = new File(newcontent.getContentTab().getContentTabInfo().getPathToFile());
		String pathTofile = file.getAbsolutePath();
		getSession().getDriver().findElement(By.id("origimagefilename")).sendKeys(pathTofile);
		//fill the display name:
		TestUtils.getInstance().clearAndType(getSession(), nameInput, newcontent.getContentTab().getContentTabInfo().getDisplayName());
		saveButton.click();
		waituntilPageLoaded(4l);
		closeButton.click();
		

	}

}
