package com.enonic.autotests.pages.v4.adminconsole.site;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Site;
import com.enonic.autotests.model.Site.AllowedPageTypes;

/**
 * @author sgauruseu
 * 
 */
public class EditSitePage extends AddSiteWizardPage
{

	@FindBy(name = "allow_label")
	private WebElement allowLabelCheckbox;
	@FindBy(name = "allow_url")
	private WebElement allowUrlCheckbox;

	@FindBy(name = "allow_section")
	private WebElement allowSectionCheckbox;

	/**
	 * The Constructor.
	 * 
	 * @param session
	 */
	public EditSitePage( TestSession session )
	{
		super(session);

	}

	/**
	 * Edit site and click by 'Save' button.
	 * @param siteName
	 * @param newSite
	 */
	public void doEditSite(String siteName, Site newSite)
	{
		AllowedPageTypes[] types = newSite.getAllowedPageTypes();
		doAllowPageTypes(types);
		saveButton.click();
	}

	/**
	 * @param types
	 */
	private void doAllowPageTypes(AllowedPageTypes[] types)
	{
		for (AllowedPageTypes type : types)
		{
			switch (type)
			{
			case LABEL:
			{
				if (!allowLabelCheckbox.isSelected())
				{
					allowLabelCheckbox.click();
				}
				break;
			}
			case SECTION:
			{
				if (!allowSectionCheckbox.isSelected())
				{
					allowSectionCheckbox.click();
				}
				break;
			}
			case URL:
			{
				if (!allowUrlCheckbox.isSelected())
				{
					allowUrlCheckbox.click();
				}
				break;
			}

			}
		}
	}

}
