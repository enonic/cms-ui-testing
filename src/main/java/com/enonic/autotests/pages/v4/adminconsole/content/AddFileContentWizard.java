package com.enonic.autotests.pages.v4.adminconsole.content;

import java.io.File;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentInfo;
import com.enonic.autotests.model.FileContentInfo;

/**
 * Page Object for 'Add File-content Wizard'
 * 
 */
public class AddFileContentWizard extends AbstractAddContentWizard implements IAddContentToRepository<FileContentInfo>
{

	/**
	 * Constructor
	 * 
	 * @param session
	 */
	public AddFileContentWizard( TestSession session )
	{
		super(session);

	}

	@Override
	public void typeDataAndSave(Content<FileContentInfo> newcontent)
	{
		ContentInfo<FileContentInfo> contentTab = newcontent.getContentTab();
		contentTab.getContentTabInfo().getComment();
		commentInput.sendKeys(contentTab.getContentTabInfo().getComment());
		descriptionTextarea.sendKeys(contentTab.getContentTabInfo().getDescription());
		// getSession().getDriver().findElement(By.id("newfile")).sendKeys("d:\\bel.gif");
		File file = new File(newcontent.getContentTab().getContentTabInfo().getPathToFile());
		String pathTofile = file.getAbsolutePath();
		getSession().getDriver().findElement(By.id("newfile")).sendKeys(pathTofile);
		saveButton.click();
		waituntilPageLoaded(4l);
		closeButton.click();

	}

}
