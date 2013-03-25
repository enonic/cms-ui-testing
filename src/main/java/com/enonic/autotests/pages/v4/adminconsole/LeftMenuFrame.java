package com.enonic.autotests.pages.v4.adminconsole;

import com.enonic.autotests.TestSession;

public class LeftMenuFrame {
	public static String CONTENT_TYPES_LOCATOR_XPATH = "//a[text()='Content types']";
	
	public ContentTypesFrame openContentTypesFrame(TestSession testSession){
		ContentTypesFrame frame = new ContentTypesFrame(testSession);
		frame.open();
		return frame;
		
	}
}
