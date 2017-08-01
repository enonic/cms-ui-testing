package com.enonic.autotests.pages.adminconsole.content;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.userstores.AclEntry;
import com.enonic.autotests.model.userstores.AclEntry.PrincipalType;
import com.enonic.autotests.model.userstores.PermissionOperation;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsoleWizardPage;
import com.enonic.autotests.pages.adminconsole.userstores.ChooseUserOrGroupPopupWindow;
import com.enonic.autotests.utils.TestUtils;

public class CreateCategoryWizard extends AbstractAdminConsoleWizardPage

{
	private final String SELECT_XPATH = "//select[@name='contenttypekey']";
	
	
	@FindBy(how = How.ID, using = "name")
	private WebElement nameInput;
	
	@FindBy(xpath = "//span[contains(@class,'tab')]/a[text()='Security']")
	private WebElement securityTab;
	
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
		// getSession().getDriver().findElements(By.xpath("//input[@name='name']"));
		if(category.getDescription()!=null)
		{
			descriptionTextArea.sendKeys(category.getDescription());
		}
		
		String contentTypeName = category.getContentTypeName();
		if (contentTypeName != null) {
			TestUtils.getInstance().selectByText(getSession(), By.xpath(SELECT_XPATH), contentTypeName);
		}
		doSetACL(category.getAclEntries());
		buttonSave.click();
		checkAlerts();
	}	
	
	public void doEditCategory(ContentCategory category, boolean isCategoryEmpty)
	{
		//do edit general tab
		//2. do edit security tab:
		doSetACL(category.getAclEntries());
		TestUtils.getInstance().saveScreenshot(getSession());
		buttonSave.click();
		checkAlerts();		
		if(category.getAclEntries()!=null && !isCategoryEmpty)
		{
			TestUtils.getInstance().saveScreenshot(getSession());
			clickByApplyButton();
		}
	}
	private void clickByApplyButton()
	{
		boolean res = TestUtils.getInstance().waitAndFind(By.xpath("//button[text()='Apply']"), getDriver());
		if(!res)
		{
			throw new TestFrameworkException("apply button was not found!");
		}
		getDriver().findElement(By.xpath("//button[text()='Apply']")).click();
	}
	
	/**
	 * @param aclentries
	 */
	public void doSetACL(List<AclEntry> aclentries)
	{		
		if(aclentries!=null)
		{
			getLogger().info("CATEGORY: goto security tab");
			securityTab.click();
			for(AclEntry entry:aclentries)
			{
				if(entry.getType().equals(PrincipalType.USER))
				{
					if(!isPrincipalPresent(entry.getPrincipalName())){
						doAddUserPrincipal(entry.getPrincipalName());
					}					
					doSetPermissions(entry);
				}
			}			
		}
		
	}
	
	private boolean isPrincipalPresent(String name){
		String xpath = String.format("//tbody[@id='accessRightTable']//td[contains(.,'%s')]", name);
		return getDriver().findElements(By.xpath(xpath)).size()>0;
		
	}
	private void doSetPermissions(AclEntry entry)
	{
		String checkboxXpath = "//tbody[@id='accessRightTable']//td[contains(.,'%s')]/..//input[contains(@name,'%s')]";
		for(PermissionOperation op: entry.getPermissions())
		{
			WebElement checkbox = getDriver().findElement(By.xpath(String.format(checkboxXpath, entry.getPrincipalName(),op.getName())));
			if(checkbox.isSelected() && !op.isAllow() )
			{
				checkbox.click();
				TestUtils.getInstance().saveScreenshot(getSession());
				continue;
			}
			if(!checkbox.isSelected() && op.isAllow() )
			{
				checkbox.click();
				TestUtils.getInstance().saveScreenshot(getSession());
				continue;
			}
			
		}
	}
	private void doAddUserPrincipal(String pname)
	{
		String addButtonXpath = "//button[@name= 'butAddAccesRightRow']";
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(addButtonXpath), getDriver());
		if(!isPresent)
		{
			throw new TestFrameworkException("button 'add'  was not found");
		}
		//click by 'add' button:
		getDriver().findElement(By.xpath(addButtonXpath)).click();
		ChooseUserOrGroupPopupWindow popup = new ChooseUserOrGroupPopupWindow(getSession());
		// choose a user
		popup.doChoosePrincipals(pname);
		waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
	}
	
}
