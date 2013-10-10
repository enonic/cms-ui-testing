package com.enonic.autotests.pages.adminconsole.content;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for a 'Source'- tab
 * 
 */
public class ContentSourceTab extends AbstractAdminConsolePage
{

	private final String TITLE_TAB = "//fieldset/legend[contains(.,'Indexed Values')]";

	/**
	 * @param session
	 */
	public ContentSourceTab( TestSession session )
	{
		super(session);

	}

	public Map<ContentIndexes, String> getIndexedVaues()
	{
		Map<ContentIndexes, String> map = new HashMap<>();
		String indexXpath = null;//
		String indexValue = null;

		for (ContentIndexes index : ContentIndexes.values())
		{
			indexXpath = String.format("//tr/td[text() ='%s']/../td[2]", index.getValue());
			boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(indexXpath), getDriver());
			if (isPresent)
			{
				indexValue = getDriver().findElement(By.xpath(indexXpath)).getText();
				map.put(index, indexValue);
			}

		}
		return map;
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TITLE_TAB)));

	}

}
