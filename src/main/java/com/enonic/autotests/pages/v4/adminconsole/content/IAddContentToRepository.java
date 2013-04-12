package com.enonic.autotests.pages.v4.adminconsole.content;

import com.enonic.autotests.model.BaseAbstractContent;
import com.enonic.autotests.model.ContentRepository;

public interface IAddContentToRepository {

	 <T extends BaseAbstractContent> void typeDataAndSave(T content);
	  //void typeDataAndSave(ContentRepository content);
}
