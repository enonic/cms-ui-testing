package com.enonic.autotests.pages.v4.adminconsole.content;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsoleWizardPage;
import com.enonic.autotests.utils.TestUtils;

public class CreateCategoryWizard extends AbstractAdminConsoleWizardPage

{
	private final String SELECT_XPATH = "//select[@name='contenttypekey']";
	@FindBy(how = How.ID, using = "name")
	private WebElement nameInput;
	
	@FindBy(how = How.NAME, using = "description")
	private WebElement descriptionTextArea;
	
	@FindBy(how = How.NAME, using ="lagre")
	private WebElement buttonSave;
	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public CreateCategoryWizard( TestSession session )
	{
		super(session);
	}
	
	public void doAddCategory(ContentCategory category)
	{
		nameInput.sendKeys(category.getName());
		//getSession().getDriver().findElements(By.xpath("//input[@name='name']"));
		if(category.getDescription()!=null)
		{
		descriptionTextArea.sendKeys(category.getDescription());
		}
		
		String contentTypeName = category.getContentTypeName();
		if (contentTypeName != null) {
			TestUtils.getInstance().selectByText(getSession(), By.xpath(SELECT_XPATH), contentTypeName);
		}
		buttonSave.click();
		checkAlerts();		
	}	
}
