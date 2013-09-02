package com.enonic.autotests.pages.v4.adminconsole.content;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;

public class ContentPropertiesTab extends AbstractAdminConsolePage
{
	private String KEY_VALUE_XPATH = "//fieldset//tr//td[contains(.,'Key:')]/../td[2]";
	public ContentPropertiesTab( TestSession session )
	{
		super(session);
		
	}

	private final String TITLE_TAB = "//fieldset/legend[contains(.,'Properties')]";

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TITLE_TAB)));
		
	}
	public String getKeyValue()
	{
		return getDriver().findElement(By.xpath(KEY_VALUE_XPATH)).getText();
	}
}
