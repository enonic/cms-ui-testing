package com.enonic.autotests.services;

import com.enonic.autotests.model.ContentType;

public interface ContentService {

	void createContentType(ContentType ctype);

	void editContentType(String name);

	void deleteContentType(String name);

}
