package com.enonic.autotests.pages.v4.adminconsole.content;

import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentRepository;

public interface IAddContentToRepository<T> {

	 void typeDataAndSave(Content<T> content);
}
