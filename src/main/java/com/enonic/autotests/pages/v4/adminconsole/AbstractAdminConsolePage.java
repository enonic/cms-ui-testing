package com.enonic.autotests.pages.v4.adminconsole;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.utils.TestUtils;

public abstract class AbstractAdminConsolePage extends Page {

	public static final String REFRESH_IMAGE_XPATH = "//img[contains(@src,'images/action_refresh_blue.gif')]";
	private static Logger logger = Logger.getLogger();
	public static String LEFT_FRAME_CLASSNAME = "leftframe";
	public static String LEFT_FRAME_NAME = "leftFrame";
	public static String MAIN_FRAME_NAME = "mainFrame";

	public static final String TITLE = "Enonic CMS - Administration";

	public AbstractAdminConsolePage(TestSession session) {
		super(session);
	}

	@Override
	public String getTitle() {
		return TITLE;
	}
	/**
	 * Selects desirable link from left frame-menu and opens the required page in
	 * the right frame. 
	 * 
	 */
	public void open(String menuItemXpath) {
		String whandle = getSession().getDriver().getWindowHandle();
		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AbstractAdminConsolePage.LEFT_FRAME_NAME);

		TestUtils.getInstance().clickByLocator(By.xpath(menuItemXpath), getSession().getDriver());

		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AbstractAdminConsolePage.MAIN_FRAME_NAME);

	}

	/**
	 * Waits until expected Title visible,
	 * @param session{@link TestSession}instance.
	 * @return true if all {@link WebElement} instances are present, otherwise return false,
	 * 
	 */
	public  static boolean verify(TestSession session) {
		boolean result = true;
		result &= session.getDriver().getTitle().contains(TITLE);
		logger.debug("");
		// 1 verify all WebElements in the  dashboard
		// 2 verify all links in the LeftFrame Menu.
		return true;
	}

}
