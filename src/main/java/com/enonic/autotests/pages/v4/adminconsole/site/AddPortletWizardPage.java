package com.enonic.autotests.pages.v4.adminconsole.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.site.Portlet;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsoleWizardPage;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentSourceTab;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Add Portlet' wizard.
 *
 */
public class AddPortletWizardPage extends AbstractAdminConsoleWizardPage
{

	private final String DATASOURCE_TAB_LINK = "//span[contains(@class,'tab')]/a[text()='Datasource']";
	
	@FindBy(name = "name")
	private WebElement nameInput;
	/**
	 * @param session
	 */
	public AddPortletWizardPage( TestSession session )
	{
		super(session);
		
	}
	public void doTypeDataAndSave(Portlet portlet)
	{
		
		if (portlet.getName() == null)
		{
			throw new IllegalArgumentException("portlet name should be specified!");
		}
		//1. fill name input
		nameInput.sendKeys(portlet.getName());
		
		doSelectStylesheet(portlet.getStylesheet().getPath(), portlet.getStylesheet().getName());
		if(portlet.getDatasource()!=null)
		{
			doSetDatasource(portlet.getDatasource());
		}
		
		saveButton.click();
		checkAlerts();

	}
	/**specifies path to stylesheet.
	 * 
	 * @param path
	 * @param resName
	 */
	private void doSelectStylesheet(String[] path, String resName)
	{
		// try to find a 'Choose' a stylesheet button
		String selectStylesheetButtonXpath = "//button[@type='button' and @name='btnstylesheet']";
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(selectStylesheetButtonXpath), getDriver());
		if (isPresent)
		{
			// click by 'Choose' a stylesheet button:(Browse)
			findElement(By.xpath(selectStylesheetButtonXpath)).click();
		}else
		{
			throw new TestFrameworkException("Button 'Choose' stylesheet was not found on the 'General' tab!");
		}
		ChooseResourcePopupWindow popup = new ChooseResourcePopupWindow(getSession());
		popup.doChooseResource(path, resName);
	}

	public void doSetDatasource(String datasource)
	{
		getDriver().findElement(By.xpath(DATASOURCE_TAB_LINK)).click();
		PortletDatasourceTab dsTab = new PortletDatasourceTab(getSession());
		dsTab.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		dsTab.setDatasource(datasource);
		
	}
	
	public String getPreviewDatasourceContent()
	{
		getDriver().findElement(By.xpath(DATASOURCE_TAB_LINK)).click();
		PortletDatasourceTab dsTab = new PortletDatasourceTab(getSession());
		dsTab.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return dsTab.getPreviewDatasourceContent();
	}

}
