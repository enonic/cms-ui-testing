package com.enonic.autotests.pages.adminconsole.site;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.site.Site;
import com.enonic.autotests.model.site.Site.AllowedPageTypes;

/**
 * Page Object for 'Edit Site' page. This page appears when "Edit" button was pressed.
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
	
	@FindBy(name = "pathtopublichome")
	protected WebElement pathToPublicResInput;
	
	@FindBy(name = "pathtohome")
	protected WebElement pathToInternalResInput;
	
	@FindBy(name = "btndeviceclassresolver")
	protected WebElement chooseDeviceResolverButton;

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
	 * 
	 * @param siteName
	 * @param newSite
	 */
	public void doEditSite(String siteName, Site newSite)
	{
		AllowedPageTypes[] types = newSite.getAllowedPageTypes();
		doAllowPageTypes(types);
		doSpecifyPathToresources(newSite.getPathToPublicResources(),newSite.getPathToInternalResources());
		if(newSite.getDeviceClassification()!=null )
		{
			doSpecifyClasificationScript(newSite.getDeviceClassification().getPath(),newSite.getDeviceClassification().getName());
		}
		
		saveButton.click();
	}

	/**
	 * Specifies path to public and internal resources.
	 * 
	 * @param pathToPublic
	 * @param pathToInternal
	 */
	private void doSpecifyPathToresources(String pathToPublic, String pathToInternal)
	{
		if(pathToPublic!=null)
		{
			pathToPublicResInput.sendKeys(pathToPublic);
		}
		if(pathToInternal!=null)
		{
			pathToInternalResInput.sendKeys(pathToInternal);
		}
		
	}
	/**
	 * Specifies a Device classification script.
	 * @param path to resource in STK.
	 * @param resName the resource name.
	 */
	private void doSpecifyClasificationScript(String[] path,String resName)
	{
		chooseDeviceResolverButton.click();
		ChooseResourcePopupWindow popup = new ChooseResourcePopupWindow(getSession());
		popup.doChooseResource(path, resName);
	}

	/**
	 * @param types
	 */
	private void doAllowPageTypes(AllowedPageTypes[] types)
	{
		if (types == null)
		{
			return;
		}
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
