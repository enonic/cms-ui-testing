package com.enonic.autotests.pages.v4.adminconsole.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.PageTemplate;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for  'Add Page Template Wizard' page.
 *
 */
public class AddPageTemplateWizard extends AbstractAdminConsolePage
{

	@FindBy(name = "name")
	private WebElement nameInput;
	
	@FindBy(name = "description")
	private WebElement descriptionTextArea;
	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public AddPageTemplateWizard( TestSession session )
	{
		super(session);
		
	}
	/**
	 * @param template
	 */
	public void doTypeDataAndSave(PageTemplate template)
	{
		if(template.getName() == null)
		{
			throw new IllegalArgumentException("template name should be specified!");
		}
		nameInput.sendKeys(template.getName());
		if(template.getDescription()!=null)
		{
			descriptionTextArea.sendKeys(template.getDescription());
		}
		TestUtils.getInstance().selectByText(getSession(), By.xpath("//slect[@name='type']"), template.getType().getValue());
		
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		// TODO Auto-generated method stub
		
	}

}
