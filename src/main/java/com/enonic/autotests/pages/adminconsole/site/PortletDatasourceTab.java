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
		boolean result = true;
		((JavascriptExecutor) getSession().getDriver()).executeScript("window.formAdminDataSource.target = \"_top\"");
		getDriver().findElement(By.xpath(PRVIEW_BUTTON_XPATH)).click();
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getDriver().getPageSource();
		//String source = getDriver().getPageSource();
//		for(String name:expectedNames)
//		{
//			result &=source.contains(name);
//		}
//		Set<String> allWindows = getDriver().getWindowHandles();
//		if (!allWindows.isEmpty())
//		{
//			for (String windowId : allWindows)
//			{
//				
//				try
//				{
//					String src;
//					String[] a = new String[2];
//					//try to find and switch to POPUP-WINDOW:
//					getDriver().switchTo().window(allWindows.toArray(a )[1]);
//					
//						String source = getDriver().getPageSource();//getDriver().getCurrentUrl() 
//						for(String name: expectedNames)//getDriver().getPageSource()
//						{                              ///getDriver().getTitle()
//							                            
//							if(!source.contains(name))
//							{
//								result &=false;
//							}
//						}
//						
//						
//					
//				} catch (NoSuchWindowException e)
//				{
//					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
//				}
//			}
//		}
		//return result;
		
	}

}
