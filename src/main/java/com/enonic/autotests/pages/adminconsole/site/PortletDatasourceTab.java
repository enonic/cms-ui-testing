package com.enonic.autotests.pages.adminconsole.site;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;

public class PortletDatasourceTab extends AbstractAdminConsolePage
{

	private final String PRVIEW_BUTTON_XPATH = "//button[@name='datasourcepreview']";

	public PortletDatasourceTab( TestSession session )
	{
		super(session);
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PRVIEW_BUTTON_XPATH)));

	}

	public void setDatasource(String datasource)
	{
		((JavascriptExecutor) getSession().getDriver()).executeScript("window.datasourcesCodeArea.codeMirror.setValue(arguments[0])", datasource);
	}

	public String getPreviewDatasourceContent()
	{
		((JavascriptExecutor) getSession().getDriver()).executeScript("window.formAdminDataSource.target = \"_top\"");
		//press the 'Preview Datasource' button
		getDriver().findElement(By.xpath(PRVIEW_BUTTON_XPATH)).click();
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			getLogger().info(e.getMessage());
		}
		return getDriver().getPageSource();
		
	}

}
