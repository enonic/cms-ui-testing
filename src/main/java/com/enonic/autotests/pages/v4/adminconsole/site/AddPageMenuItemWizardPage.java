package com.enonic.autotests.pages.v4.adminconsole.site;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.site.PageMenuItem;

/**
 *Page Object for AddPageMenuItemWizard.
 *
 */
public class AddPageMenuItemWizardPage extends AbstractMenuItemWizardPage<PageMenuItem>
{

	/**
	 * The constructor
	 * 
	 * @param session
	 */
	public AddPageMenuItemWizardPage( TestSession session )
	{
		super(session);
		
	}

	@Override
	public void doTypeDataAndSave(PageMenuItem pageItem)
	{
		String dispalayName = pageItem.getDisplayName();
		if(dispalayName == null )
		{
			throw new IllegalArgumentException("display name should not be null !");
		}
		displaynameInput.sendKeys(dispalayName);
		String menuname = pageItem.getMenuName();
		if(menuname !=null )
		{
			menunameInput.sendKeys(menuname);
		}
		boolean isShow = pageItem.isShowInMenu();
		if((!showInMenuCheckBox.isSelected() && isShow) || (!isShow && showInMenuCheckBox.isSelected()))
		{
			showInMenuCheckBox.click();
		}
		saveButton.click();	
		
	}

	

	

}
