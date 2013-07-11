package com.enonic.autotests.testdata.content;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "contents")
@XmlType(propOrder = { "name", "type", "required"}, name = "select")
public class ContentSelectXml {

	
}
