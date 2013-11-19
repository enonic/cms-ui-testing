package com.enonic.autotests.pages.adminconsole.content;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.AddContentException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentWithEditorInfo;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.model.TinyMCETable;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.utils.TestUtils;

/**
 * Content Wizard page, that contains TinyMCE HTML WYSIWYG editor.
 * 
 */
public class ContentWithTinyMCEWizard extends AbstractAddContentWizard<ContentWithEditorInfo> 
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
	private final String INSERT_TABLE_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_table')]";
	private final String INSERT_SPECIAL_SYMBOLS_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_charmap')]";
	private final String INSERT_DELETION_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_del')]";
	private final String INSERT_INSERTION_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_ins')]";
	private final String INSERT_BLOCKQUOTE_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_blockquote')]";
	private final String INSERT_ABBREVIATION_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_abbr')]";
	private final String INSERT_ACRONYM_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_acronym')]";
	private final String INSERT_CITATION_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,' mce_cite')]";
	private final String INSERT_SUPERSCRIPT_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_sup')]";
	private final String INSERT_SUBSCRIPT_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_sub')]";
	private final String INSERT_BULLIST_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_bullist')]";
	private final String INSERT_NUMLIST_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_numlist')]";
	private final String INCREASE_INDENT_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_indent')]";
	private final String DECREASE_INDENT_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_outdent')]";
	private final String UNDO_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_undo')]";
	private final String REDO_BUTTON_XPATH = "//td[contains(@class,'mceToolbar')]//a[@role='button' and contains(@class,'mce_redo')]";
	
	private String EDIT_HTML_BUTTON_XPATH = "//a[contains(@class,'mceButtonEnabled ') and @title='Edit HTML Source']";
	
	

	private final String IFRAME = "//td[contains(@class,'mceIframeContainer')]//iframe";
	private final String SELECT_CONTENT_POPUP_WINDOW_TITLE = "Enonic CMS - Content repository";

	private final String TINY_MCE_INNERHTML = "return document.getElementsByTagName('iframe')[0].contentDocument.body.innerHTML;";

	private final String ADD_ANCHOR_POPUP_TITLE = "Insert/Edit Anchor";
	private final String INSERT_LINK_POPUP_TITLE = "Insert/edit link";

	private final String INSERT_TABLE_POPUP_TITLE = "Insert/Edit Table";
	private final String INSERT_SPECIAL_CHARACTER_POPUP_TITLE = "Select Special Character";
	private final String INSERT_DELETION_POPUP_TITLE = "Deletion Element";
	private final String INSERT_INSERTION_POPUP_TITLE = "Insertion Element";
	private final String INSERT_ABBREVIATION_POPUP_TITLE = "Abbreviation Element";
	private final String INSERT_CITATION_POPUP_TITLE = "Citation Element";
	private final String INSERT_ACRONYM_POPUP_TITLE = "Acronym Element";

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

	@FindBy(xpath = INSERT_TABLE_BUTTON_XPATH)
	private WebElement insertTableButton;

	@FindBy(xpath = INSERT_SPECIAL_SYMBOLS_BUTTON_XPATH)
	private WebElement insertSpecSymbolsButton;

	@FindBy(xpath = INSERT_DELETION_BUTTON_XPATH)
	private WebElement insertDeletionButton;

	@FindBy(xpath = INSERT_INSERTION_BUTTON_XPATH)
	private WebElement insertInsertionButton;

	@FindBy(xpath = INSERT_BLOCKQUOTE_BUTTON_XPATH)
	private WebElement insertBlockQouteButton;

	@FindBy(xpath = INSERT_ABBREVIATION_BUTTON_XPATH)
	private WebElement insertAbbreviationButton;

	@FindBy(xpath = INSERT_ACRONYM_BUTTON_XPATH)
	private WebElement insertAcronymButton;

	@FindBy(xpath = INSERT_CITATION_BUTTON_XPATH)
	private WebElement insertCitationButton;

	@FindBy(xpath = INSERT_SUPERSCRIPT_BUTTON_XPATH)
	private WebElement insertSuperscriptButton;
	
	@FindBy(xpath = INSERT_SUBSCRIPT_BUTTON_XPATH)
	private WebElement insertSubscriptButton;
	
	@FindBy(xpath = INSERT_BULLIST_BUTTON_XPATH)
	private WebElement insertBulListButton;
	
	@FindBy(xpath = INSERT_NUMLIST_BUTTON_XPATH)
	private WebElement insertNumListButton;
	
	@FindBy(xpath = INCREASE_INDENT_BUTTON_XPATH)
	private WebElement increaseIndentButton;
	
	@FindBy(xpath = DECREASE_INDENT_BUTTON_XPATH)
	private WebElement decreaseIndentButton;
	
	@FindBy(xpath = REDO_BUTTON_XPATH)
	private WebElement redoButton;
	
	@FindBy(xpath = UNDO_BUTTON_XPATH)
	private WebElement undoButton;

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public ContentWithTinyMCEWizard( TestSession session )
	{
		super(session);

	}
	 public boolean isPresentEditHtmlButton()
	 {
		 return TestUtils.getInstance().waitAndFind(By.xpath(EDIT_HTML_BUTTON_XPATH), getDriver());
	 }
	
	@Override
	public void typeDataAndSave(Content<ContentWithEditorInfo> content)
	{
		boolean result = TestUtils.getInstance().waitAndFind(By.xpath("//input[@name='myTitle']"), getDriver());
		if(!result)
		{
			throw new TestFrameworkException("input  myTitle was not found");
		}
		getDriver().findElement(By.xpath("//input[@name='myTitle']")).sendKeys(content.getDisplayName());
		editorArea.sendKeys(content.getContentTab().getInfo().getHtmlareaText());
		
		doSetACL(content.getAclEntries());
		saveButton.click();
		closeButton.click();
				
	}
	public void verifyUndoRedo()
	{
		selectAlignment(AlignmentText.LEFT);
		undoButton.click();
		String string1 = "string1";
		
		editorArea.sendKeys(string1);
		editorArea.sendKeys("\n");
		String string2 = "string2";
		editorArea.sendKeys(string2);
		
		String expectedText = string1+ " " +string2;
		boolean isPresent1 = verifyTextInEditor(string1);
		boolean isPresent2 = verifyTextInEditor(string2);
		//1. both string should be present
		if(!isPresent1 && !isPresent2 )
		{
			Assert.fail("actual innerHtml does not contain expected string: " + expectedText);
		}
		
		undoButton.click();
		isPresent1 = verifyTextInEditor(string1);
		isPresent2 = verifyTextInEditor(string2);
		if(!isPresent1 && isPresent2 )
		{
			Assert.fail("Undo action failed, only string1 should be present in the editor  " + expectedText);
		}
		redoButton.click();
		isPresent1 = verifyTextInEditor(string1);
		isPresent2 = verifyTextInEditor(string2);
		if(!isPresent1 && !isPresent2 )
		{
			Assert.fail("Redo action failed, both strings should be present in editor " + expectedText);
		}
	
		
	}
	/**
	 * Types a string, clicks by 'Increase Indent' button , verify style: padding-left: 30px; click by 'Decrease Indent' after that.
	 */
	public void verifyIncreaseDecreaseIndents()
	{
		
		selectAlignment(AlignmentText.LEFT);
		undoButton.click();
		String text = "test text";
		editorArea.sendKeys(text);
		increaseIndentButton.click();
		String expectedText = "<p style=\"padding-left: 30px;\">"+text;
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected string: " + expectedText);
		}
		
		decreaseIndentButton.click();
		expectedText = "padding-left: 30px;";
		isPresent = verifyTextInEditor(expectedText);
		if(isPresent)
		{
			Assert.fail("actual innerHtml contains string: " + expectedText+ "but this text should be present!");
		}
	}
	
	/**
	 * Clicks by 'Bulleted List' button and types two strings.
	 */
	public void verifyInsertBulletedList()
	{
		insertBulListButton.click();
		String text1 = "test1";
		String text2 = "test2";
		editorArea.sendKeys(text1);
		editorArea.sendKeys("\n");
		editorArea.sendKeys(text2);
				
		String[] expectedText = { "<ul><li>"+ text1, text2 , "</li></ul>" };
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings: ");
		}
	}

	

	/**
	 * Clicks by 'Numbered List' button and types two strings.
	 */
	public void verifyInsertNumberedList()
	{
		insertNumListButton.click();
		String text1 = "test1";
		String text2 = "test2";
		editorArea.sendKeys(text1);
		editorArea.sendKeys("\n");
		editorArea.sendKeys(text2);
				
		String[] expectedText = { "<ol><li>", text1, text2,"</li></ol>" };
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings: ");
		}
	}

	/**
	 * Types text, select all and clicks by 'Superscript' button
	 */
	public void verifyInsertSuperscript()
	{
		selectAlignment(AlignmentText.LEFT);
		String text = "superscript test";
		editorArea.sendKeys(text);
		selectAll(); 
		insertSuperscriptButton.click();
		String expectedText =  "<sup>"+ text ;
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected string: " + expectedText);
		}
	}

	/**
	 * Types text, select all and clicks by 'Subscript' button
	 */
	public void verifyInsertSubscript()
	{
		selectAlignment(AlignmentText.LEFT);
		String text = "subscript test";
		editorArea.sendKeys(text);
		
		selectAll();
		insertSubscriptButton.click();
		String expectedText =  "<sub>"+ text+"</sub>" ;
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings: "+expectedText);
		}
	}
	/**
	 * Clicks by 'Insert Special Characters' button, and verify: character is
	 * present.
	 */
	public void verifyInsertSpecialCharacters(SpecialCharacters character)
	{
		selectAlignment(AlignmentText.LEFT);
		insertSpecSymbolsButton.click();
		doAddSpecialCharacter(character);

		char[] chars = Character.toChars(Integer.valueOf(character.getValue()));
		String expectedText = new String(chars);
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings: "+ expectedText);
		}
		

	}

	/**
	 * Verifies insert citation
	 */
	public void verifyInsertCitation()
	{
		selectAlignment(AlignmentText.LEFT);
		undoButton.click();
		String text = "deleteion test";
		editorArea.sendKeys(text);
		selectAll();
		insertCitationButton.click();
		doAddCitation();
		String[] expectedText = { "<cite", text , "</cite>" };

		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings ");
		}
	}

	public void doAddCitation()
	{
		Set<String> allWindows = getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getDriver().switchTo().window(windowId).getTitle().equals(INSERT_CITATION_POPUP_TITLE))
					{

						// 1. click by 'Insert' button
						findElement(By.xpath("//input[@type='submit' and @name='insert']")).click();

						// 2. switch to mainFrame again:
						getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );

						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}

	}

	/**
	 *  Verifies insert deletion
	 */
	public void verifyDeletion()
	{
		selectAlignment(AlignmentText.LEFT);
		String text = "deleteion test";
		editorArea.sendKeys(text);
		selectAll();
		insertDeletionButton.click();
		doAddDeletion();
		String[] expectedText = { "<del", text + "</del>" };

		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected string: ");
		}
	}

	/**
	 *  Verifies insert insertion
	 */
	public void verifyInsertion()
	{
		selectAlignment(AlignmentText.LEFT);
		String text = "isertion test";
		editorArea.sendKeys(text);
		selectAll();
		insertInsertionButton.click();
		doAddInsertion();
		String[] expectedText = { "<ins", text + "</ins>" };
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings ");
		}
	}

	/**
	 *  Verifies insert 'Block Quote'
	 */
	public void verifyInsertBlockQuote()
	{
		selectAlignment(AlignmentText.LEFT);
		undoButton.click();
		String text = "text test";
		editorArea.sendKeys(text);
		selectAll();
		insertBlockQouteButton.click();
		String[] expectedText = {"<blockquote>" , text};
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected string: "+ expectedText);
		}
	}

	/**
	 * Verifies insert 'Abbreviation'
	 */
	public void verifyInsertAbbreviation()
	{
		
		selectAlignment(AlignmentText.LEFT);
		undoButton.click();
		
		String text = "text test";
		editorArea.sendKeys(text);
		selectAll();
		insertAbbreviationButton.click();
		doInsertAbbreviation();
		String[] expectedText = { "<abbr", text, "</abbr>" };
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings ");
		}
	}

	/**
	 * Verifies insert 'Acronym'
	 */
	public void verifyInsertAcronym()
	{
		selectAlignment(AlignmentText.LEFT);
		String text = "text test";
		editorArea.sendKeys(text);
		selectAll();
		insertAcronymButton.click();
		doInsertAcronum();
		String[] expectedText = { "<acronym", text + "</acronym>" };
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings ");
		}
	}

	private void doInsertAcronum()
	{
		Set<String> allWindows = getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getDriver().switchTo().window(windowId).getTitle().equals(INSERT_ACRONYM_POPUP_TITLE))
					{

						// 1. click by 'Insert' button
						findElement(By.xpath("//input[@type='submit' and @name='insert']")).click();

						// 2. switch to mainFrame again:
						getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );

						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}

	}

	/**
	 * type data and click by "Insert" button
	 */
	public void doInsertAbbreviation()
	{
		Set<String> allWindows = getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getDriver().switchTo().window(windowId).getTitle().equals(INSERT_ABBREVIATION_POPUP_TITLE))
					{

						// 1. click by 'Insert' button
						findElement(By.xpath("//input[@type='submit' and @name='insert']")).click();

						// 2. switch to mainFrame again:
						getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );

						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}

	}

	private void doAddInsertion()
	{

		Set<String> allWindows = getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getDriver().switchTo().window(windowId).getTitle().equals(INSERT_INSERTION_POPUP_TITLE))
					{
						// 1. type a current date:
						findElement(By.xpath("//a/span[@class='datetime']")).click();
						// 2. click by 'Insert' button
						findElement(By.xpath("//input[@type='submit' and @name='insert']")).click();

						// 3. switch to mainFrame again:
						getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );

						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}

	}

	/**
	 * Switches to the popup window, types data and press "Insert" button.
	 */
	private void doAddDeletion()
	{
		Set<String> allWindows = getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getDriver().switchTo().window(windowId).getTitle().equals(INSERT_DELETION_POPUP_TITLE))
					{
						// 1. type a current date:
						findElement(By.xpath("//input[@name='datetime']")).click();
						// 2. click by 'Insert' button
						findElement(By.xpath("//input[@type='submit' and @name='insert']")).click();

						// 3. switch to mainFrame again:
						getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );

						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}

	}

	/**
	 * Switches to the popup window, selects a character and press "Insert" button.
	 * 
	 * @param character
	 */
	private void doAddSpecialCharacter(SpecialCharacters character)
	{

		Set<String> allWindows = getDriver().getWindowHandles();

		if (!allWindows.isEmpty())
		{
			String whandle = getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getDriver().switchTo().window(windowId).getTitle().equals(INSERT_SPECIAL_CHARACTER_POPUP_TITLE))
					{

						// 1. select a character in popup window:
						String charXpath = String.format("//a[@class='charmaplink' and contains(@onfocus,'%s')]", character.getValue());
						boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(charXpath), getDriver());
						if (!isPresent)
						{
							Assert.fail("The character : " + character.getValue() + " was not found!, please check xpath");
						}
						findElement(By.xpath(charXpath)).click();
						// 2. switch to main frame again:s
						getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );

						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}
	}

	/**
	 * Clicks by 'Insert Image' button, add image and verify: image is present on the page.
	 */
	public void verifyInsertImage(Content<ImageContentInfo> contentToInsert)
	{
		// 1. open popup window
		insertImageButton.click();
		// 2. select an image:
		doInsertImage(contentToInsert);
		// verify: image is present in Editor HTML code:
		String[] expectedText = { String.format("<img title=\"%s\"", contentToInsert.getDisplayName()) };
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings ");
		}
	}

	/**
	 * Switches to the popup window, select a image and press "Insert" button.
	 * 
	 * @param contentToInsert
	 */
	private void doInsertImage(Content<ImageContentInfo> contentToInsert)
	{
		String[] path = contentToInsert.getParentNames();
		Set<String> allWindows = getDriver().getWindowHandles();

		if (!allWindows.isEmpty())
		{
			String whandle = getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getDriver().switchTo().window(windowId).getTitle().equals(SELECT_CONTENT_POPUP_WINDOW_TITLE))
					{
						for (int i = 0; i < path.length - 1; i++)
						{
							// expands repository and parent categories:
							String expanderFolderXpath = String.format("//td/span[contains(@id,'menuitemText') and contains(.,'%s')]/../../td/a/img",
									path[i]);
							boolean result = TestUtils.getInstance().expandFolder(getSession(), expanderFolderXpath);
							if (!result)
							{
								Assert.fail("Error during expanding a folder: " + path[i]);
							}
						}
						// click by category and open
						String categoryXpath = String.format("//a[descendant::span[contains(@id,'menuitemText') and contains(.,'%s')]]",
								path[path.length - 1]);
						boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(categoryXpath), getDriver());
						if (!isPresent)
						{
							throw new AddContentException("Category with name " + path[path.length - 1] + " was not found!");
						}
						// click by category and open Table of content:
						findElement(By.xpath(categoryXpath)).click();
						// switch to MAINFRAME in popup-window:
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );
						ContentsTableFrame contentsTable = new ContentsTableFrame(getSession());
						contentsTable.waituntilPageLoaded(1l);
						// select checkbox for content and press the butoon 'Add'
						contentsTable.doChooseContent(contentToInsert.getDisplayName());
						boolean isFound = TestUtils.getInstance().waitAndFind(By.xpath("//select[@name='size']"), getDriver());
						if (!isFound)
						{
							Assert.fail("TinyMCE EDITOR: Insert image failed, //select[@name='size']  was not found!");
						}
						TestUtils.getInstance().selectByText(getSession(), By.xpath("//select[@name='size']"), "Thumbnail");
						findElements(By.xpath("//button[@type='button' and @name='insert']")).get(0).click();
						// popup window closed, so nedd switch to parent window
						getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );

						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}
	}

	/**
	 * Inserts a table and verify: table with defined height and width is present in Editor
	 */
	public void verifyInsertTable()
	{
		insertTableButton.click();
		TinyMCETable tableToInsert = new TinyMCETable();
		tableToInsert.setColumnsNumber(4);
		tableToInsert.setRowsNumber(4);
		int height = 70;
		int width = 70;
		tableToInsert.setHeight(height);
		tableToInsert.setWidth(width);
		doInsertTable(tableToInsert);
		// verify:
		String[] expectedText = { "<table", String.format("height: %dpx;", 70), String.format("width: %dpx", width) };
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected string: ");
		}
	}

	/**
	 * Switches to a "Insert/Edit Table" popup window and types columns number ,
	 * rows number... etc
	 * 
	 * @param tableToInsert
	 */
	private void doInsertTable(TinyMCETable tableToInsert)
	{
		boolean isPopupFound = false;
		Set<String> allWindows = getSession().getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getSession().getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getSession().getDriver().switchTo().window(windowId).getTitle().equals(INSERT_TABLE_POPUP_TITLE))
					{
						isPopupFound = true;
						List<WebElement> elems = findElements(By.xpath("//input[@type='text' and @name='cols']"));
						if (elems.size() == 0)
						{
							Assert.fail("input text with name ='cols' was not found!");
						}
						// 1. insert columns number.
						TestUtils.getInstance().clearAndType(getSession(), elems.get(0), String.valueOf(tableToInsert.getColumnsNumber()));

						// 2.insert rows number.
						elems = findElements(By.xpath("//input[@type='text' and @name='rows']"));
						if (elems.size() == 0)
						{
							Assert.fail("input text with name ='rows' was not found!");
						}

						TestUtils.getInstance().clearAndType(getSession(), elems.get(0), String.valueOf(tableToInsert.getRowsNumber()));
						// 3. insert Width.
						elems = findElements(By.xpath("//input[@type='text' and @name='width']"));
						if (elems.size() == 0)
						{
							Assert.fail("input text with name ='width' was not found!");
						}

						elems.get(0).sendKeys(String.valueOf(String.valueOf(tableToInsert.getWidth())));

						// 4. insert Height.
						elems = findElements(By.xpath("//input[@type='text' and @name='height']"));
						if (elems.size() == 0)
						{
							Assert.fail("input text with name ='height' was not found!");
						}

						elems.get(0).sendKeys(String.valueOf(String.valueOf(tableToInsert.getHeight())));

						// 2. press the submit button
						elems = findElements(By.xpath("//input[@type='submit' and @name='insert']"));
						elems.get(0).click();
						// popup window closed, need to switch to the main window
						getSession().getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );
						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
			if (!isPopupFound)
			{
				Assert.fail("Popup window was not found!!!");
			}

		}
	}

	/**
	 * Clicks by 'Insert Link' and add new link. Unliks just added link and
	 * verify: link disappeared.
	 */
	public void verifyLinkUnlink()
	{
		String linkText = "google.com";
		// 1. click by Link button and create new link:
		doAddLink(linkText);
		// verify:
		//String expectedText = String.format("<a href=\"http://%s\"", linkText);
		boolean isPresent = verifyTextInEditor("href",linkText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings "+ linkText);
		}
		// 2. select all
		selectAll();
		// 3. click by unlink button
		unlinkButton.click();
		// 4. verify
		//expectedText = String.format("<p>google.com", linkText);
		isPresent = verifyTextInEditor("<href",linkText);
		if(isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected string ");
		}
	}

	/**
	 * Switches to a "Insert/Edit Link" popup window and types text and click
	 * 'insert' button.
	 * 
	 * @param linkText
	 */
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
						if (elems.size() == 0)
						{
							System.out.println("");
						}
						// 1. insert text to input.
						elems.get(0).sendKeys(linkText);
						elems = findElements(By.xpath("//input[@type='text' and @name='input-standard']"));
						String url = String.format("http://%s", linkText);
						// 1. insert URL to input.
						TestUtils.getInstance().clearAndType(getSession(), elems.get(0), url);

						// 2. press the submit button
						elems = findElements(By.xpath("//input[@type='submit' and @name='insert']"));
						elems.get(0).click();
						// popup window closed, need to switch to the main window
						getSession().getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );
						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}

	}

	/**
	 * Gets HTML code from TinyMCE editor and verify: expected strings are
	 * present in the code
	 * 
	 * @param expected
	 */
	private boolean verifyTextInEditor(String... expected)
	{
		boolean result = true;
		Object obj = ((JavascriptExecutor) getSession().getDriver()).executeScript(TINY_MCE_INNERHTML);
		String actualInnerHtml = obj.toString();
		for (String s : expected)
		{
			result &= actualInnerHtml.contains(s);

		}
		return result;

	}

	/**
	 * Clicks by 'Insert Horizontal Line' button, and verify it.
	 */
	public void verifyHorizontalLine()
	{
		insertLineButton.click();
		String expectedText = "<hr>";
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings ");
		}
	}

	/**
	 * Clicks by 'Insert Anchor' button, types data in popup-window and verify
	 * Anchor is present in the text
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
						if (elems.size() == 0)
						{
							Assert.fail("Anchor item was not foun in toolbar");
						}
						// 1. insert text to input.
						elems.get(0).sendKeys(anchorText);
						// 2. press the submit button
						elems = findElements(By.xpath("//input[@type='submit' and @name='insert']"));
						elems.get(0).click();
						// popup window closed, need to switch to the main window
						getSession().getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );
						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}

		//String expectedText = String.format("<p><a class=\"mceItemAnchor\" name=\"%s\"></a></p>", anchorText);
		boolean isPresent = verifyTextInEditor("mceItemAnchor", anchorText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings: ");
		}
	

	}

	public void verifyChangeColorText()
	{
		selectAlignment(AlignmentText.LEFT);
		String text = "test text";
		editorArea.sendKeys(text);
		//editorArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		selectAll();
		List<WebElement> elems = findElements(By.xpath("//a[@title='Select Text Color' and descendant::span[@class='mceIconOnly']]"));
		elems.get(0).click();
		TestUtils.getInstance().waitAndFind(By.xpath("//a[@title='Red']"), getDriver());
		elems = findElements(By.xpath("//a[@title='Red']"));
		elems.get(0).click();
		String expectedText = "color: rgb(255, 0, 0);";
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected strings ");
		}

	}

	public void verifyChangeBackgroundColorText()
	{
		//Actions builder = new Actions(getDriver());
		//WebElement elem = getDriver().findElement(By.xpath("//td[contains(@class,'mceIframeContainer')]//iframe"));
		//builder.moveToElement(elem).click().build().perform();
		selectAlignment(AlignmentText.LEFT);
		String text = "test text";
		editorArea.sendKeys(text);
		selectAll();
		//elem.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		List<WebElement> elems = findElements(By.xpath("//a[@title='Select Background Color' and descendant::span[@class='mceIconOnly']]"));
		elems.get(0).click();
		TestUtils.getInstance().waitAndFind(By.xpath("//a[@title='Red']"), getDriver());
		elems = findElements(By.xpath("//a[@title='Red']"));
		elems.get(0).click();
		String expectedText = "background-color: rgb(255, 0, 0);";
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected string: ");
		}

	}

	/**
	 * Clicks by Alignment Center
	 */
	public void verifyTextAlignment(AlignmentText align)
	{
		selectAlignment(AlignmentText.LEFT);
		undoButton.click();
		String p1 = "test text";
		String p2 = "alignment test";
		String text = p1 + "\n" + p2;
		editorArea.sendKeys(text);
		selectAll();
	//	editorArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		selectAlignment(align);
		String expected = String.format("<p style=\"text-align: %s;\">", align.getValue()) + p1;
		Object obj = ((JavascriptExecutor) getSession().getDriver()).executeScript(TINY_MCE_INNERHTML);
		String actualInnerHtml = obj.toString();
		boolean result = actualInnerHtml.contains(expected);
		if (!result)
		{
			Assert.fail("actual innerHtml does not contain expected string: " + expected);
		}
		expected = String.format("<p style=\"text-align: %s;\">", align.getValue()) + p2;
		result = actualInnerHtml.contains(expected);
		if (!result)
		{
			Assert.fail("actual innerHtml does not contain expected string: " + expected);
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
	 * Click by 'Bold', 'Italic' and 'Remove Formatting' buttons and verify
	 * innerHtml
	 */
	public void verifyItalicBoldAndRemoveFormatting()
	{
		selectAlignment(AlignmentText.LEFT);
		undoButton.click();
		String text = "test text";
		editorArea.sendKeys(text);
		selectAll();
		

		boldButton.click();
		String expectedText = "<strong>" + text + "</strong>";
		boolean isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected string: ");
		}
		removeFormatButton.click();

		italicButton.click();
		expectedText = "<em>" + text + "</em>";
		isPresent = verifyTextInEditor(expectedText);
		if(!isPresent)
		{
			Assert.fail("actual innerHtml does not contain expected string ");
		}
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GENERAL_TAB_LINK_XPATH)));

	}

	private void selectAll()
	{
		if(getSession().getIsRemote())
		{
			editorArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		} else
		{
			String os = System.getProperty("os.name").toLowerCase();
			if (os.indexOf("mac") >= 0)
			{
				editorArea.sendKeys(Keys.chord(Keys.COMMAND, "a"));
			} else
			{
				editorArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			}
		}
		

	}

}
