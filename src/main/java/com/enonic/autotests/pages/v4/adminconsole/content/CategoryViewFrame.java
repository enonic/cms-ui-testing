package com.enonic.autotests.pages.v4.adminconsole.content;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;

/**
 * When category selected from the left menu, this Frame will appears.
 *
 */
public  class CategoryViewFrame extends AbstractContentTableView
{

	private static final  String CHECKALL_XPATH = "//img[@id='batch_operation_image']";
	
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
	public CategoryViewFrame( TestSession session )
	{
		super(session);

	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		getSession().getDriver().findElements(By.xpath(SELECT_TOP_XPATH));
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CHECKALL_XPATH)));

	}
	
	/**
	 *  Clicks by 'select all' checkbox and chooses "Delete" action from the drop-down list.
	 */
	public void doDeleteAllContent()
	{
		checkAllcheckbox.click();
		boolean isEnabled = TestUtils.getInstance().waitUntilClickableNoException(getSession(), By.xpath(SELECT_TOP_XPATH), AppConstants.IMPLICITLY_WAIT);
		if(!isEnabled)
		{
			throw new TestFrameworkException("The 'select-All' element is disabled or wrong xpath");
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

}
