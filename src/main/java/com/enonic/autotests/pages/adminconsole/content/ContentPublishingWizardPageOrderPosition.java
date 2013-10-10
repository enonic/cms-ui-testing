package com.enonic.autotests.pages.adminconsole.content;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

public class ContentPublishingWizardPageOrderPosition extends AbstractContentTableView
{
	private final String HREF_TITLE_XPATH = "//a[text()='Step 2-1 of 3: Position content in the section']";
	private final String ALL_ROWS_XPATH = "//tr[contains(@class,'tablerowpainter')]";
	private String CONTENT_MOVE_DOWN_ICON_XPATH = "//tr[contains(@class,'tablerowpainter') and descendant::b[text()='%s']]//a/img[@src='images/icon_move_down.gif']";
	
	@FindBy(name = "next")
	private WebElement nextButton;

	/**
	 * @param session
	 */
	public ContentPublishingWizardPageOrderPosition( TestSession session )
	{
		super(session);
	}

	public void doNext()
	{
		nextButton.click();
	}
	
	public void moveToEnd(String contentName)
	{
		List<WebElement> rows = findElements(By.xpath(ALL_ROWS_XPATH));
		getLogger().info("ContentPublishingWizard, ordered section:table contains "+ rows.size() +" rows");
		for(int i=0;i<rows.size()-1;i++)
		{
			clickByArrow(contentName);
		}
	}
	private void clickByArrow(String name)
	{
		String contentArrowXpath = String.format(CONTENT_MOVE_DOWN_ICON_XPATH, name);
		List<WebElement> elems = findElements(By.xpath(contentArrowXpath));
		if(elems.size() == 0)
		{
			throw new TestFrameworkException("Content move down failed, content or arrow was not found ");
		}
		elems.get(0).click();
		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e)
		{
			getLogger().warning("clickByArrow method, InterruptedException");
		}
	}
	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(HREF_TITLE_XPATH)));
		
	}

}
