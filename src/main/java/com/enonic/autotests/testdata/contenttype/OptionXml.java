package com.enonic.autotests.testdata.contenttype;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "select")
@XmlType(propOrder = { "value", "type", "required"}, name = "option")
public class OptionXml {

}
