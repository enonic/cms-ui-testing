package com.enonic.autotests.pages.v4.adminconsole.content;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.BaseAbstractContent;
import com.enonic.autotests.model.FilesContent;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.TextTransfer;

/**
 * 
 * 11.04.2013
 */
public class AddFileContentWizard extends AbstractAdminConsolePage implements IAddContentToRepository {

	@FindBy(how = How.ID, using = "_comment")
	protected WebElement commentInput;

	@FindBy(how = How.ID, using = "newfile")
	protected WebElement newfileInput;

	@FindBy(how = How.ID, using = "description")
	protected WebElement descriptionTextarea;

	@FindBy(how = How.ID, using = "keywords")
	protected WebElement keywordsInput;

	@FindBy(how = How.ID, using = "saveSplitButtonBottomleftButton")
	protected WebElement sabeButton;

	public AddFileContentWizard(TestSession session) {
		super(session);

	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BaseAbstractContent> void typeDataAndSave(T content) {

		FilesContent fileContent = (FilesContent) content;
		Actions builder = new Actions(getSession().getDriver());

		Action myAction = builder.click(getSession().getDriver().findElement(By.id("newfile"))).release().build();

		myAction.perform();

		String path = TestUtils.getInstance().createTempFile("Hello");
		TextTransfer textTransfer = new TextTransfer();
		textTransfer.setClipboardContents(path);

		Robot robot = null;

		Set<String> handles = getSession().getDriver().getWindowHandles();
		Iterator<String> iter = handles.iterator();
		// iterate through your windows
		while (iter.hasNext()) {
			String parent = iter.next();
			System.out.println(parent);

		}
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);

		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		sabeButton.click();
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("OK");

	}

	// @Override
	// public void typeDataAndSave(FileContent fileContent) {
	// // TODO Auto-generated method stub
	//
	// }

}
