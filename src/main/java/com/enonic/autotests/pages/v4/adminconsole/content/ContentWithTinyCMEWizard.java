package com.enonic.autotests.pages.v4.adminconsole.content;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;

public class ContentWithTinyCMEWizard extends AbstractContentTableView
{
	private final String BOLD_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_bold')]";
	private final String ITALIC_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_italic')]";
	private final String REMOVE_FORMAT_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_removeformat')]";
	

	@FindBy(xpath = BOLD_BUTTON_XPATH)
	private WebElement boldButton;
	
	@FindBy(xpath = ITALIC_BUTTON_XPATH)
	private WebElement italicButton;
	
	@FindBy(xpath = REMOVE_FORMAT_BUTTON_XPATH)
	private WebElement removeFormatButton;
	
	public ContentWithTinyCMEWizard( TestSession session )
	{
		super(session);
		
	}
	
	public void verifyItalicBoldAndRemoveFormatting()
	{
		
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe//body[@class='mceContentBody']")));
		
	}

}
