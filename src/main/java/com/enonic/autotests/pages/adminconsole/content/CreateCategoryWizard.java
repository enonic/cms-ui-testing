package com.enonic.autotests.pages.adminconsole.content;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.userstores.AclEntry;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsoleWizardPage;
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
	
	public void doEditCategory(ContentCategory category)
	{
		//do edit general tab
		//2. do edit security tab:
		doChangeACL(category.getAclEntries());
		buttonSave.click();
		checkAlerts();		
	}
	
	/**
	 * @param aclentries
	 */
	public void doChangeACL(List<AclEntry> aclentries)
	{
		if(aclentries!=null && !aclentries.isEmpty())
		{
			
		}
		
	}
	
}
