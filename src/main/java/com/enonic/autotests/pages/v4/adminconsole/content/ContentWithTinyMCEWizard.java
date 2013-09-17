package com.enonic.autotests.pages.v4.adminconsole.content;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.PageNavigatorV4;
import com.enonic.autotests.utils.TestUtils;

/**
 * Content Wizard page, that contains TinyMCE HTML WYSIWYG editor.
 *
 */
public class ContentWithTinyMCEWizard extends AbstractAdminConsolePage
{
	private final String BOLD_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_bold')]";
	private final String ITALIC_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_italic')]";
	private final String REMOVE_FORMAT_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_removeformat')]";
	private final String ALIGN_CENETR_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_justifycenter')]";
	private final String ALIGN_LEFT_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_justifyleft')]";
	private final String ALIGN_RIGHT_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_justifyright')]";
	private final String ALIGN_FULL_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_justifyfull')]";
	private final String ANCHOR_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_anchor')]";
	private final String UNLINK_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_unlink')]";
	private final String LINK_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_cmslink')]";
	private final String INSERT_LINE_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a/span[contains(@class,'mce_hr')]";
	private final String INSERT_IMAGE_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_cmsimage')]";


	private final String IFRAME = "//td[contains(@class,'mceIframeContainer')]//iframe";
	
	private final String TINY_MCE_INNERHTML = "return document.getElementsByTagName('iframe')[0].contentDocument.body.innerHTML;";
	
	private final String ADD_ANCHOR_POPUP_TITLE = "Insert/Edit Anchor";
	private final String INSERT_LINK_POPUP_TITLE = "Insert/edit link";

	@FindBy(xpath = BOLD_BUTTON_XPATH)
	private WebElement boldButton;
	
	@FindBy(xpath = ITALIC_BUTTON_XPATH)
	private WebElement italicButton;
	
	@FindBy(xpath = REMOVE_FORMAT_BUTTON_XPATH)
	private WebElement removeFormatButton;
	
	@FindBy(xpath = IFRAME)
	private WebElement editorArea;
	
	@FindBy(xpath = ALIGN_CENETR_BUTTON_XPATH)
	private WebElement alignCenterButton;
	
	@FindBy(xpath = ALIGN_LEFT_BUTTON_XPATH)
	private WebElement alignLeftButton;
	
	@FindBy(xpath = ALIGN_RIGHT_BUTTON_XPATH)
	private WebElement alignRightButton;
	
	@FindBy(xpath = ALIGN_FULL_BUTTON_XPATH)
	private WebElement alignFullButton;
	
	@FindBy(xpath = ANCHOR_BUTTON_XPATH)
	private WebElement anchorButton;
	
	@FindBy(xpath = UNLINK_BUTTON_XPATH)
	private WebElement unlinkButton;
	
	@FindBy(xpath = LINK_BUTTON_XPATH)
	private WebElement linkButton;
	
	
	@FindBy(xpath = INSERT_LINE_BUTTON_XPATH)
	private WebElement insertLineButton;
	
	@FindBy(xpath = INSERT_IMAGE_BUTTON_XPATH)
	private WebElement insertImageButton;
	
	
	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public ContentWithTinyMCEWizard( TestSession session )
	{
		super(session);
		
	}
	/**
	 * Clicks by 'Insert Image' button, add image and verify: image is present on the page.
	 */
	public void verifyAddImage()
	{
		
	}
	public void verifyAddTable()
	{
		
	}
	/**
	 * Clicks by 'Insert Link' and add new link. Unliks just added link and verify: link disappeared.
	 */
	public void verifyLinkUnlink()
	{
		String linkText = "google.com";
		//1. click by Link button and create new link:
		doAddLink(linkText);
		// verify: 
		String expected =String.format("<a href=\"http://%s\"",linkText);
		verifyTextInEditor(expected);
		//2. select all
		selectAll();
		//3. click by unlink button
		unlinkButton.click();
		//4. verify
		expected =String.format("<p>google.com",linkText);
		verifyTextInEditor(expected);
	}
	private void doAddLink(String linkText)
	{
		linkButton.click();
		Set<String> allWindows = getSession().getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getSession().getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getSession().getDriver().switchTo().window(windowId).getTitle().equals(INSERT_LINK_POPUP_TITLE))
					{
						List<WebElement> elems = findElements(By.xpath("//input[@type='text' and @name='link-text']"));
						if(elems.size() == 0)
						{
							System.out.println("");
						}
						//1. insert text to input.
						elems.get(0).sendKeys(linkText);
						elems = findElements(By.xpath("//input[@type='text' and @name='input-standard']"));
						String url = String.format("http://%s", linkText);
						//1. insert URL to input.
						TestUtils.getInstance().clearAndType(getSession(), elems.get(0), url);
						
						//2. press the submit button
						elems = findElements(By.xpath("//input[@type='submit' and @name='insert']"));
						elems.get(0).click();
						// popup window closed, need to switch to the main  window
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
	private void verifyTextInEditor(String expected)
	{
		
		Object obj =((JavascriptExecutor) getSession().getDriver()).executeScript(TINY_MCE_INNERHTML);
		String actualInnerHtml = obj.toString();
		boolean result = actualInnerHtml.contains(expected);
		if(!result)
		{
			Assert.fail("actual innerHtml does not contain expected string: "+ expected);
		}
	}
	/**
	 * Clicks by 'Insert Horizontal Line' button, and verify it.
	 */
	public void verifyHorizontalLine()
	{
		insertLineButton.click();
		String expected ="<hr>";
		verifyTextInEditor(expected);
	}
	/**
	 * Clicks by 'Insert Anchor' button, types data in popup-window and verify Anchor is present in the text
	 */
	public void verifyAnchorInText()
	{
		String anchorText = "anchortest";
		anchorButton.click();
		Set<String> allWindows = getSession().getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getSession().getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getSession().getDriver().switchTo().window(windowId).getTitle().equals(ADD_ANCHOR_POPUP_TITLE))
					{
						List<WebElement> elems = findElements(By.xpath("//input[@type='text' and @name='anchorName']"));
						if(elems.size() == 0)
						{
							System.out.println("");
						}
						//1. insert text to input.
						elems.get(0).sendKeys(anchorText);
						//2. press the submit button
						elems = findElements(By.xpath("//input[@type='submit' and @name='insert']"));
						elems.get(0).click();
						// popup window closed, need to switch to the main  window
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

		
		String expected =String.format("<p><a class=\"mceItemAnchor\" name=\"%s\"></a></p>",anchorText);
		verifyTextInEditor(expected);
		editorArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));	
		
	}
	
	/**
	 * Clicks by Alignment Center
	 */
	public void verifyTextAlignment(AlignmentText align)
	{
		String p1 = "test text";
		String p2 = "alignment test";
		String text = p1+"\n" + p2;
		editorArea.sendKeys(text);
		editorArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));	
		selectAlignment(align);
		String expected =String.format("<p style=\"text-align: %s;\">",align.getValue())+p1;
		Object obj =((JavascriptExecutor) getSession().getDriver()).executeScript(TINY_MCE_INNERHTML);
		String actualInnerHtml = obj.toString();
		boolean result = actualInnerHtml.contains(expected);
		if(!result)
		{
			Assert.fail("actual innerHtml does not contain expected string: "+ expected);
		}
		expected =String.format("<p style=\"text-align: %s;\">",align.getValue())+p2;
		result = actualInnerHtml.contains(expected);
		if(!result)
		{
			Assert.fail("actual innerHtml does not contain expected string: "+ expected);
		}
	}
	private void selectAlignment(AlignmentText alignValue)
	{
		switch (alignValue)
		{
		case RIGHT:
		{
			alignRightButton.click();
			break;
		}
		
		case LEFT:
		{
			alignLeftButton.click();
			break;
		}
		case FULL:
		{
			alignFullButton.click();
			break;
		}
		case CENTER:
		{
			alignCenterButton.click();
			break;
		}
			
		default:
			break;
		}
	}
	
	/**
	 * Click by 'Bold', 'Italic' and 'Remove Formatting' buttons and verify innerHtml
	 */
	public void verifyItalicBoldAndRemoveFormatting()
	{
		String text = "test text";
		editorArea.sendKeys(text);
		editorArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));		
		
		boldButton.click();
		String expected = "<strong>"+text+"</strong>";
		verifyTextInEditor(expected );
		removeFormatButton.click();

		italicButton.click();
		expected = "<em>"+text+"</em>";
		verifyTextInEditor(expected);
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe//body[@class='mceContentBody']")));
		
	}
	private void selectAll()
	{
		editorArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));
	}

}
