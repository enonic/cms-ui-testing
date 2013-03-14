package com.enonic.autotests.pages.v4;

import org.openqa.selenium.WebDriver;

import com.enonic.autotests.pages.Page;

public class AdminConsoolePage extends Page {

	public static String LEFT_FRAME_CLASSNAME = "leftframe";

	private final String title = "Enonic CMS - Administration";

	public AdminConsoolePage(WebDriver driver) {
		setDriver(driver);
		// PageFactory.initElements(driver, this);
	}

	@Override
	public String getTitle() {
		return title;
	}

}
