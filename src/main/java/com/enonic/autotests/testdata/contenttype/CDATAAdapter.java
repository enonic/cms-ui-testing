package com.enonic.autotests.testdata.contenttype;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CDATAAdapter extends XmlAdapter<String, String> {
	 
    @Override
    public String marshal(String cfgData) throws Exception {
        return "<![CDATA[" + cfgData + "]]>";
    }
 
    @Override
    public String unmarshal(String cfgData) throws Exception {
        return cfgData;
    }

}
