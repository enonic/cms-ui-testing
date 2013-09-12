package com.enonic.autotests.pages.v4.adminconsole.content.search;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SearchException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.utils.TestUtils;

public class SearchContentPage extends AbstractAdminConsolePage
{
	private final String IN_ATTACHMENTS_RADIO_XPATH = "//input[@type='radio' and @value='fileAttachments']";
	private final String TITLE_FIELD_ONLY_RADIO_XPATH = "//input[@type='radio' and @value='title']";
	private final String ALL_FIELDS_ONLY_RADIO_XPATH = "//input[@type='radio' and @value='all']";
	private final String SEARCH_BUTTON_XPATH = "//button[@type='submit' and text()='Search']";
	
	@FindBy(name = "asearchtext")
	private WebElement searchTextInput;

	@FindBy(xpath = IN_ATTACHMENTS_RADIO_XPATH)
	private WebElement attachmentRadio;
	
	@FindBy(xpath = TITLE_FIELD_ONLY_RADIO_XPATH)
	private WebElement titleFieldRadio;
	
	@FindBy(xpath = ALL_FIELDS_ONLY_RADIO_XPATH)
	private WebElement allFieldsRadio;
	
	@FindBy(xpath = SEARCH_BUTTON_XPATH)
	private WebElement searchButton;
	/**
	 * @param session
	 */
	public SearchContentPage( TestSession session )
	{
		super(session);

	}

	/**
	 * Types search params and clicks by 'Search' button. Waits until table with content appears in the bottom of the page.
	 * @param params
	 */
	public List<String> doAdvancedSearch(ContentSearchParams params)
	{
		String searchText = params.getSearchText();
		if(searchText != null)
		{
			searchTextInput.sendKeys(searchText);
		}
		if(params.getWhere().equals(SearchWhere.ATTACHMENTS))
		{
			attachmentRadio.click();
			
		}else if(params.getWhere().equals(SearchWhere.ALL_FIELDS))
		{
			allFieldsRadio.click();
			
		}else if(params.getWhere().equals(SearchWhere.TITLE_FIELD_ONLY))
		{
			titleFieldRadio.click();
		}
		searchButton.click();
		TestUtils.getInstance().scrollWindow(getSession(),0, 700);
		TestUtils.getInstance().saveScreenshot(getSession());
		
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 1l);
		if(isAlertPresent)
		{
			throw new SearchException("alert dialog appeared when 'Search' button was pressed");
		}
		boolean isResultPresent = TestUtils.getInstance().waitAndFind(By.xpath("//fieldset//td[@class='browsetablecolumnheader']"), getDriver());
		if(!isResultPresent)
		{
			throw new SearchException("Search failed! Table with content does not present on page!");
		}
		List<WebElement> names = getSession().getDriver().findElements(By.xpath(RepositoriesListFrame.SPAN_CONTENTS_NAME_XPATH));
		List<String> contentNames = new ArrayList<>();
		for(WebElement elems: names)
		{
			contentNames.add(elems.getText());
		}
		return contentNames;
		
	}
	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(IN_ATTACHMENTS_RADIO_XPATH)));

	}

}
