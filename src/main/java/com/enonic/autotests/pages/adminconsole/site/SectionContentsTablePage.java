package com.enonic.autotests.pages.adminconsole.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.AddContentException;
import com.enonic.autotests.exceptions.RemoveCMSObjectException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page object for 'Section Menu Item'.
 * 
 * <br>
 * Path to page: Sites / siteName / Menu items / section name/
 * 
 */
public class SectionContentsTablePage extends AbstractAdminConsolePage
{

	private final String SELECT_CONTENT_POPUP_WINDOW_TITLE = "Enonic CMS - Content repository";
	
	private String CONTENT_TITLECOLUMN_XPATH = "//tr[contains(@class,'tablerowpainter')]//td/div[text()='%s']";
	private final String UNAPPROVE_ICON = "images/icon_content_unapprove.gif";
	private final String APPROVE_ICON = "images/icon_content_approve.gif";
	private String CONTENT_APPROVE_ICON_XPATH = "//tr[contains(@class,'tablerowpainter') and descendant::td/div[text()='%s']]//a/img[@src='%s']";

	private String VIEW_CONTENTS_SELECT_XPATH = "//select[@name='browsemode']";
	
	private String ALL_NAMES_DIV_XPATH ="//div[@style ='font-weight: bold']";

	private String TABLE_TITLE_COLUMN_XPATH = "//table//tr//td[text()='Title']";
	private String TABLE_CONTENT_TYPE_COLUMN_XPATH = "//table//tr//td[text()='Content type']";
	
	private final String REMOVE_ITEM_DROPDOWN_LIST = "Remove selected";

	/** add content o menu item button */
	@FindBy(xpath = "//button[text()='Add']")
	private WebElement buttonAdd;
	
	private final String SELECT_ACTION_XPATH ="//select[@name='batchSelector']";
	
	private String POPUP_WINDOW_ADD_CONTENT_TO_SECTION_FOLDER = "//td[child::span[contains(@id,'menuitemText') and contains(.,'%s')]]";
	
	private String POPUP_WINDOW_ADD_CONTENT_TO_SECTION_FOLDER_EXPANDER = POPUP_WINDOW_ADD_CONTENT_TO_SECTION_FOLDER + "/..//a/img[contains(@src,'plus')]";//"/../../td/a/img";
	
	
	private String CONTENT_CHECKBOX_XPATH = "//tr[contains(@class,'tablerowpainter') and descendant::div[contains(@style,'font-weight: bold') and text()='%s']]/td/input[@name='batch_operation']";

	
	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public SectionContentsTablePage( TestSession session )
	{
		super(session);

	}
	
	/**
	 * Gets all names of content, located in a section.
	 * @return
	 */
	public List<String> getAllContentNames()
	{
		List<String> allNames = new ArrayList<>();
		List<WebElement> tdElements = findElements(By.xpath(ALL_NAMES_DIV_XPATH));
		for(WebElement el:tdElements)
		{
			allNames.add(el.getText());
		}
		
		return allNames;
	}
	/**
	 * Selects a checkbox for content, selects "Remove selected" from drop down list and  removes content from section.
	 * 
	 * @param contentDisplayName name of content to delete from section.
	 */
	public void doRemoveContent(String contentDisplayName)
	{
		String checkboxXpath = String.format(CONTENT_CHECKBOX_XPATH,contentDisplayName);
		boolean result = TestUtils.getInstance().waitAndFind(By.xpath(checkboxXpath), getDriver());
		if(!result)
		{
			throw new RemoveCMSObjectException("content to delete was not found "+ contentDisplayName);
		}
		//1 select a checkbox
		findElement(By.xpath(checkboxXpath)).click();
		boolean isEnabled = TestUtils.getInstance().waitUntilClickableNoException(getSession(), By.xpath(SELECT_ACTION_XPATH), 2l);
		if(!isEnabled)
		{
			throw new TestFrameworkException("Select action drop down list is disabled!");
		}
		//2. select item from drop down list.
		TestUtils.getInstance().selectByText(getSession(), By.xpath(SELECT_ACTION_XPATH), REMOVE_ITEM_DROPDOWN_LIST);
		//3. wait until content disappears from table.
		String contentTypeXpath = String.format(CONTENT_TITLECOLUMN_XPATH, contentDisplayName);
		result = TestUtils.getInstance().waitUntilInvisibleNoException(getSession(), By.xpath(contentTypeXpath), 2l);
		if(!result)
		{
			throw new RemoveCMSObjectException("content was not deleted "+ contentDisplayName);
		}
	}

	/**
	 * @param pathToContent path to content.
	 * @param contentDisplayName.
	 */
	public void doAddContentToSection(String[] pathToContent,String contentDisplayName)
	{
		buttonAdd.click();
		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e1)
		{
			
		}
		Set<String> allWindows = getDriver().getWindowHandles();

		if (!allWindows.isEmpty())
		{
			String whandle = getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getDriver().switchTo().window(windowId).getTitle().equals(SELECT_CONTENT_POPUP_WINDOW_TITLE))
					{
						String repoName = pathToContent[0];
						String categoryName = pathToContent[1];
						
						//expands repository and parent categories:
						String expanderFolderXpath = String.format(POPUP_WINDOW_ADD_CONTENT_TO_SECTION_FOLDER_EXPANDER, repoName);
						TestUtils.getInstance().expandFolder(getSession(),expanderFolderXpath);
						
						// click by category and open 

						String categoryXpath = String.format(POPUP_WINDOW_ADD_CONTENT_TO_SECTION_FOLDER, repoName) +String.format("/../..//a[child::span[text()='filesCategory']]",categoryName);
						boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(categoryXpath), getDriver());
						if(!isPresent)
						{
							throw new AddContentException("Category with name "+pathToContent[pathToContent.length - 1]+" was not found!");
						}
						//click by category and open Table of content:
						findElement(By.xpath(categoryXpath)).click();
						//switch to MAINFRAME in popup-window:
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );
						ContentsTableFrame contentsTable = new ContentsTableFrame(getSession());
						contentsTable.waituntilPageLoaded(1l);
						// select checkbox for content and press the butoon 'Add'
						contentsTable.doAddContentToSection(contentDisplayName);
						
						//popup window closed, so nedd switch to parent window
						getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );
						
						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}

	}

	/**
	 * Verifies content present in the table.
	 * 
	 * @param contentTitle
	 * @return true if content present in table, otherwise false.
	 */
	public boolean verifyIsPresent(String contentTitle)
	{
		String contentTypeXpath = String.format(CONTENT_TITLECOLUMN_XPATH, contentTitle);
		getSession().getDriver().manage().timeouts().implicitlyWait(AppConstants.IMPLICITLY_WAIT, TimeUnit.SECONDS);
		List<WebElement> elements = getSession().getDriver().findElements(By.xpath(contentTypeXpath));
		if (elements.size() == 0)
		{
			getLogger().info("content  with name  " + contentTitle + "was not found!");
			return false;
		}

		getLogger().info("new Content Type was found in the Table! " + contentTitle);
		return true;
	}

	/**
	 * Verify is content published.
	 * @param contentTitle
	 * @return true if content published, otherwise false.
	 */
	public boolean isContentPublished(String contentTitle)
	{
		String contentUpproveIcon = String.format(CONTENT_APPROVE_ICON_XPATH, contentTitle, APPROVE_ICON);
		getSession().getDriver().manage().timeouts().implicitlyWait(AppConstants.IMPLICITLY_WAIT, TimeUnit.SECONDS);
		List<WebElement> upproveIconelement = findElements(By.xpath(contentUpproveIcon));
		
			
		String contentUnupproveIcon = String.format(CONTENT_APPROVE_ICON_XPATH, contentTitle, UNAPPROVE_ICON);
		List<WebElement> unUpproveIconelement = findElements(By.xpath(contentUnupproveIcon));
		if (upproveIconelement.size() > 0)
		{
			getLogger().info("content  with name  " + contentTitle + "is UNPUBLISHED!");
			return false;
		}
		else if (unUpproveIconelement.size() > 0)
		{
			getLogger().info("content  with name  " + contentTitle + "is PUBLISHED!");
			return true;
		}
		else{
			getLogger().info("new Content  was found in the Table or wrong xpath for 'approve and publish icon'" + contentTitle);
			throw new TestFrameworkException();
		}
		
	}

	/**
	 * Switches view to 'View:Contents'
	 */
	public void switchToViewContent()
	{
		
		TestUtils.getInstance().selectByText(getSession(), By.xpath(VIEW_CONTENTS_SELECT_XPATH), "View: Contents");
		
		new WebDriverWait(getSession().getDriver(), 2l)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TABLE_CONTENT_TYPE_COLUMN_XPATH)));

	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TABLE_TITLE_COLUMN_XPATH)));
	}

}
