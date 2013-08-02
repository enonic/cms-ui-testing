package com.enonic.autotests.pages.v4.adminconsole.content;

import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.PageNavigatorV4;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Category View'
 * 
 * this Frame appears when category selected from the left menu. Frame contains
 * a table with content.
 * 
 */
public class ContentsTableFrame extends AbstractContentTableView
{
    private final String MOVE_CONTENT_POPUP_WINDOW_TITLE = "Choose destination category";
	private static final String CHECKALL_XPATH = "//img[@id='batch_operation_image']";
	private String POPUP_WINDOW_DESTINATION_CATEGORY_XPATH = "//a[contains(@title,'%s')]";
	private final String DELETE_CATEGORY_BUTTON = "//a[@class='button_link']/button[text()='Delete']";
	
	private String POPUP_WINDOW_DESTINATION_REPO_EXPANDER_XPATH ="//tr[contains(@id,'tr-category') and descendant::td[contains(.,'%s')]]/td[1]/a";

	@FindBy(how = How.ID, using = "batch_operation_image")
	protected WebElement checkAllcheckbox;

	@FindBy(how = How.ID, using = "movecategorybtn")
	protected WebElement moveButton;

	private final String SELECT_TOP_XPATH = "//table[@class='operation-top']//select[@name='batchSelector']";

	/**
	 * Constructor
	 * 
	 * @param session
	 */
	public ContentsTableFrame( TestSession session )
	{
		super(session);

	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		getSession().getDriver().findElements(By.xpath(SELECT_TOP_XPATH));
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='browsetable']")));

	}

	public void doMoveContent(String contentName, String repoName, String destinationFolderName)
	{
		String moveLinkXpath = String.format(MOVE_CONTENT_LINK, contentName);
		if (!TestUtils.getInstance().waitAndFind(By.xpath(moveLinkXpath), getSession().getDriver()))
		{
			throw new TestFrameworkException("move link was not found or wrong xpath");
		}
		getSession().getDriver().findElement(By.xpath(moveLinkXpath)).click();
		//wait
		Set<String> allWindows = getSession().getDriver().getWindowHandles();
		
	    if(!allWindows.isEmpty()) {
	    	String whandle = getSession().getDriver().getWindowHandle();
	        for (String windowId : allWindows)
	        {
	        
	            try {
	                if(getSession().getDriver().switchTo().window(windowId).getTitle().equals(MOVE_CONTENT_POPUP_WINDOW_TITLE)) {
	                	clickByDestinationFolder(repoName, destinationFolderName);
	                    //popup window closed, need to switch to the main window
	                	getSession().getDriver().switchTo().window(whandle);
	                	PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
	                    break;
	                }
	            }
	            catch(NoSuchWindowException e) {
	                throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
	            }
	        }
	    }
	        
	        
		
	}
	private void clickByDestinationFolder(String repoName,String destinationFolderName)
	{
		System.out.println("START clickByDestinationFolder");
		//1. expand the Repository Folder.
		String repoExpanderPath = String.format(POPUP_WINDOW_DESTINATION_REPO_EXPANDER_XPATH, repoName);
		if (!TestUtils.getInstance().waitAndFind(By.xpath(repoExpanderPath), getSession().getDriver()))
		{
			throw new TestFrameworkException("destination Repository does not exists or wrong xpath");
		}
		getSession().getDriver().findElement(By.xpath(repoExpanderPath)).click();
		
		//2. click by destination: Category Folder.
		String destinationFolderXpath = String.format(POPUP_WINDOW_DESTINATION_CATEGORY_XPATH,destinationFolderName);
		if (!TestUtils.getInstance().waitAndFind(By.xpath(destinationFolderXpath), getSession().getDriver()))
		{
			throw new TestFrameworkException("destination folder does not exists or wrong xpath");
		}
		
		
		try
		{
		getSession().getDriver().findElement(By.xpath(destinationFolderXpath)).click();
		}catch(Exception e){
			System.out.println("move content popup window was closed");
		}
		
		System.out.println("#########################################################  clickByDestinationFolder finished");

	}

	/**
	 * Deletes content from the table of contents. Clicks by 'Delete' link and
	 * confirm deletion.
	 * 
	 * @param contentDisplayName
	 */
	public void doDeleteContent(String contentDisplayName)
	{
		String deleteContenIcon = String.format(DELETE_CONTENT_LINK, contentDisplayName);
		// 1. verify delete-icon is present.
		if (!TestUtils.getInstance().waitAndFind(By.xpath(deleteContenIcon), getSession().getDriver()))
		{
			throw new TestFrameworkException("delete link was not found or wrong xpath");
		}
		// 2. click by 'Delete' icon
		WebElement elem = getSession().getDriver().findElement(By.xpath(deleteContenIcon));
		elem.click();
		// 3.confirm deletion
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 1l);
		if (isAlertPresent)
		{
			Alert alert = getSession().getDriver().switchTo().alert();
			// Get the Text displayed on Alert:
			String textOnAlert = alert.getText();
			getLogger().info("Deleting of the content , alert message:" + textOnAlert);
			// Click OK button, by calling accept() method of Alert Class:
			alert.accept();
		}
		waituntilPageLoaded(2l);
	}

	/**
	 * Clicks by 'select all' checkbox and chooses "Delete" action from the
	 * drop-down list.
	 */
	public void doDeleteAllContent()
	{
		checkAllcheckbox.click();
		//TestUtils.getInstance().waitIsDispalyedElement(elem, getSession().getDriver())
		boolean isClickable = TestUtils.getInstance().waitUntilClickableNoException(getSession(), By.xpath(SELECT_TOP_XPATH),
				AppConstants.IMPLICITLY_WAIT);
		if (!isClickable)
		{
			//throw new TestFrameworkException("The 'select-All' element is disabled or wrong xpath");
			getLogger().info("The category is empty");
			return;
		}
		TestUtils.getInstance().selectByText(getSession(), By.xpath(SELECT_TOP_XPATH), "Delete");
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 1l);
		if (isAlertPresent)
		{
			Alert alert = getSession().getDriver().switchTo().alert();
			// Get the Text displayed on Alert:
			String textOnAlert = alert.getText();
			getLogger().info("Deletion of contents, alert message:" + textOnAlert);
			// Click OK button, by calling accept() method of Alert Class:
			alert.accept();
		}

	}
	
	public void doDeleteEmptyCategory()
	{
		boolean isDeleteButtonPresent = TestUtils.getInstance().waitUntilClickableNoException(getSession(), By.xpath(DELETE_CATEGORY_BUTTON), 2l);
		if(!isDeleteButtonPresent)
		{
			throw new TestFrameworkException("Delete category button is not clickable! Wrong xpath or category is not empty!");
		}
		getSession().getDriver().findElement(By.xpath(DELETE_CATEGORY_BUTTON)).click();
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 1l);
		if (isAlertPresent)
		{
			Alert alert = getSession().getDriver().switchTo().alert();
			// Get the Text displayed on Alert:
			String textOnAlert = alert.getText();
			getLogger().info("Deletion of category, alert message:" + textOnAlert);
			alert.accept();
		}
		
	}

}
