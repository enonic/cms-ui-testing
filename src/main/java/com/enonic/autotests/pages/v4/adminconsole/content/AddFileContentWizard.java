package com.enonic.autotests.pages.v4.adminconsole.content;

import java.io.File;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.IContentInfo;
import com.enonic.autotests.model.FileContentInfo;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Add File-content Wizard'
 * 
 */
public class AddFileContentWizard extends AbstractAddContentWizard implements IUpdateOrCreateContent<FileContentInfo>
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
		IContentInfo<FileContentInfo> contentTab = newcontent.getContentTab();
		String comment = contentTab.getContentTabInfo().getComment();
		if(comment !=null &&!commentInput.getAttribute("value").equals(comment))
		{
		commentInput.sendKeys(contentTab.getContentTabInfo().getComment());
		}
		if(contentTab.getContentTabInfo().getDescription()!=null && !descriptionTextarea.getAttribute("value").equals(contentTab.getContentTabInfo().getDescription()))
		{
		descriptionTextarea.sendKeys(contentTab.getContentTabInfo().getDescription());
		}
		
		File file = new File(newcontent.getContentTab().getContentTabInfo().getPathToFile());
		String pathTofile = file.getAbsolutePath();
		if(!findElement(By.id("newfile")).getText().equals(pathTofile))
		{
			findElement(By.id("newfile")).sendKeys(pathTofile);
		}
		//fill the display name:
		if(!nameInput.getAttribute("value").equals( newcontent.getDisplayName()))
		{
			TestUtils.getInstance().clearAndType(getSession(), nameInput, newcontent.getDisplayName());
			
		}
		
				
		saveButton.click();
		waituntilPageLoaded(4l);
		closeButton.click();

	}

}
