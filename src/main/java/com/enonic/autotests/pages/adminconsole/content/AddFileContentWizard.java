package com.enonic.autotests.pages.adminconsole.content;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.FileContentInfo;
import com.enonic.autotests.model.IContentInfo;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Add File-content Wizard'
 *
 */
public class AddFileContentWizard extends AbstractAddContentWizard<FileContentInfo> 
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
		String comment = contentTab.getInfo().getComment();
		if(comment !=null &&!commentInput.getAttribute("value").equals(comment))
		{
			commentInput.sendKeys(contentTab.getInfo().getComment());
		}
		if(contentTab.getInfo().getDescription()!=null && !descriptionTextarea.getAttribute("value").equals(contentTab.getInfo().getDescription()))
		{
			descriptionTextarea.sendKeys(contentTab.getInfo().getDescription());
		}
				
		String pathTofile = newcontent.getContentTab().getInfo().getPathToFile();
		URL dirURL = ContentConvertor.class.getClassLoader().getResource(pathTofile);
		File file = null;
		try
		{
			file = new File(dirURL.toURI());
		} catch (URISyntaxException e)
		{
			getLogger().error("Error during importing a content: Wrong file URL ", getSession());

		}
		WebElement fileInputType = findElement(By.id("newfile"));
		if (!getSession().getIsRemote())
		{

			fileInputType.sendKeys(file.getAbsolutePath());
		}
		else
		{
			LocalFileDetector detector = new LocalFileDetector();
			
			File localFile = detector.getLocalFile(file.getAbsolutePath());
			((RemoteWebElement) fileInputType).setFileDetector(detector);

			fileInputType.sendKeys(localFile.getAbsolutePath());
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
