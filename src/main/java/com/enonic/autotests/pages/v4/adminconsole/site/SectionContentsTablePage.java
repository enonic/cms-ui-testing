package com.enonic.autotests.pages.v4.adminconsole.site;

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
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.services.PageNavigatorV4;
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
	private String CONTENT_TITLE_XPATH = "//tr[contains(@class,'tablerowpainter_darkrow')]//td/div[text()='%s']";

	private String VIEW_CONTENTS_SELECT_XPATH = "//select[@name='browsemode']";

	private String TABLE_TITLE_COLUMN_XPATH = "//table//tr//td[text()='Title']";
	private String TABLE_CONTENT_TYPE_COLUMN_XPATH = "//table//tr//td[text()='Content type']";

	/** add content o menu item button */
	@FindBy(xpath = "//button[text()='Add']")
	private WebElement buttonAdd;
	private String POPUP_WINDOW_ADD_CONTENT_TO_SECTION_FOLDER = "//a[descendant::span[contains(@id,'menuitemText') and contains(.,'%s')]]";
	
	private String POPUP_WINDOW_ADD_CONTENT_TO_SECTION_FOLDER_EXPANDER = POPUP_WINDOW_ADD_CONTENT_TO_SECTION_FOLDER + "/../../td/a/img";;

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
	 * @param parents
	 * @param contentDisplayName
	 */
	public void doAddContentToSection(String[] parents,String contentDisplayName)
	{
		buttonAdd.click();
		Set<String> allWindows = getSession().getDriver().getWindowHandles();

		if (!allWindows.isEmpty())
		{
			String whandle = getSession().getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getSession().getDriver().switchTo().window(windowId).getTitle().equals(SELECT_CONTENT_POPUP_WINDOW_TITLE))
					{
						for(int i = 0; i< parents.length - 1; i++)
						{
							//expands repository and parent categories:
							String expanderFolderXpath = String.format(POPUP_WINDOW_ADD_CONTENT_TO_SECTION_FOLDER_EXPANDER, parents[i]);
							TestUtils.getInstance().expandFolder(getSession(),expanderFolderXpath);
						}
						// click by category and open 
						String categoryXpath = String.format(POPUP_WINDOW_ADD_CONTENT_TO_SECTION_FOLDER, parents[parents.length - 1]);
						boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(categoryXpath), getSession().getDriver());
						if(!isPresent)
						{
							throw new AddContentException("Category with name "+parents[parents.length - 1]+" was not found!");
						}
						//click by category and open Table of content:
						getSession().getDriver().findElement(By.xpath(categoryXpath)).click();
						//switch to MAINFRAME in popup-window:
						PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
						ContentsTableFrame contentsTable = new ContentsTableFrame(getSession());
						contentsTable.waituntilPageLoaded(1l);
						// select checkbox for content and press the butoon 'Add'
						contentsTable.doAddContentToSection(contentDisplayName);
						
						//popup window closed, so nedd switch to parent window
						getSession().getDriver().switchTo().window(whandle);
						PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
						
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
		String contentTypeXpath = String.format(CONTENT_TITLE_XPATH, contentTitle);
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
	 * Switches view to 'View:Contents'
	 */
	public void switchToViewContent()
	{
		try
		{
			TestUtils.getInstance().selectByText(getSession(), By.xpath(VIEW_CONTENTS_SELECT_XPATH), "View:Contents");
		} catch (NoSuchElementException ex)
		{
			getLogger().error(" The option with name 'View:Contents' is absent", getSession());
			return;
		}
		new WebDriverWait(getSession().getDriver(), 2l)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TABLE_CONTENT_TYPE_COLUMN_XPATH)));

	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TABLE_TITLE_COLUMN_XPATH)));
	}

}
