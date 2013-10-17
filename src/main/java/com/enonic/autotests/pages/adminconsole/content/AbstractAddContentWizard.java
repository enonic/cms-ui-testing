package com.enonic.autotests.pages.adminconsole.content;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.userstores.AclEntry;
import com.enonic.autotests.model.userstores.AclEntry.PrincipalType;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.adminconsole.userstores.ChooseUserOrGroupPopupWindow;
import com.enonic.autotests.utils.TestUtils;

public abstract class AbstractAddContentWizard<T> extends AbstractAdminConsolePage implements IContentWizard<T>
{

	public static final String GENERAL_TAB_LINK_XPATH = "//h1//a[text()='Content']";

	// 1 input: draft offline: unsaved draft
	@FindBy(how = How.ID, using = "_comment")
	protected WebElement commentInput;

	// 2. description text area
	@FindBy(how = How.NAME, using = "description")
	protected WebElement descriptionTextarea;

	// 3. key words input text
	@FindBy(how = How.ID, using = "keywords")
	protected WebElement keywordsInput;
	// 4. save button
	@FindBy(how = How.ID, using = "saveSplitButtonBottomleftButton")
	protected WebElement saveButton;
	//5. close wizard button
	@FindBy(how = How.ID, using = "closebtn")
	protected WebElement closeButton;
	
	@FindBy(how = How.ID, using = "name")
	protected WebElement nameInput;
	
	@FindBy(xpath = "//span[contains(@class,'tab')]/a[text()='Security']")
	private WebElement securityTab;
	
	private final String PROPERTIES_TAB_LINK = "//span[contains(@class,'tab')]/a[text()='Properties']";
	private final String SOURCE_TAB_LINK = "//span[contains(@class,'tab')]/a[text()='Source']";
	

	/**
	 * Constructor
	 * 
	 * @param session
	 */
	public AbstractAddContentWizard( TestSession session )
	{
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GENERAL_TAB_LINK_XPATH)));

	}
	@Override
	public String getContentKey()
	{
		getDriver().findElement(By.xpath(PROPERTIES_TAB_LINK)).click();
		ContentPropertiesTab propTab = new ContentPropertiesTab(getSession());
		propTab.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return propTab.getKeyValue();
		
	}
	@Override
	public Map<ContentIndexes,String> getIndexedValues()
	{
		getDriver().findElement(By.xpath(SOURCE_TAB_LINK)).click();
		ContentSourceTab sourceTab = new ContentSourceTab(getSession());
		sourceTab.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sourceTab.getIndexedVaues();
	}

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
					doAddUserPrincipal(entry.getPrincipalName());
					doSetPermissions(entry);
				}
			}
			
		}
		
	}
	private void doSetPermissions(AclEntry entry)
	{
		String checkboxXpath = "//tbody[@id='accessRightTable']//td[contains(.,'%s')]/..//input[contains(@name,'%s')]";
		for(String op: entry.getPermissions())
		{
			WebElement checkbox = getDriver().findElement(By.xpath(String.format(checkboxXpath, entry.getPrincipalName(),op)));
			if(!checkbox.isSelected() && entry.isAllow() )
			{
				checkbox.click();
				TestUtils.getInstance().saveScreenshot(getSession());
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
	
	@Override
	public abstract  void typeDataAndSave(Content<T> content);
	
}
