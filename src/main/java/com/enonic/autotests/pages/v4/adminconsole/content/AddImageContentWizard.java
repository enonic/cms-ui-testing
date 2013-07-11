package com.enonic.autotests.pages.v4.adminconsole.content;

import java.io.File;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentInfo;
import com.enonic.autotests.model.ImageContentInfo;

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
		contentTab.getContentTabInfo().getComment();
	
		File file = new File(newcontent.getContentTab().getContentTabInfo().getPathToFile());
		String pathTofile = file.getAbsolutePath();
		getSession().getDriver().findElement(By.id("origimagefilename")).sendKeys(pathTofile);
		saveButton.click();
		waituntilPageLoaded(4l);
		closeButton.click();
		

	}

}
