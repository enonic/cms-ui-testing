package com.enonic.autotests.providers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.enonic.autotests.model.CustomContent.InputModel;

public class InputHandler extends DefaultHandler {

	private List <InputModel> inputs = new ArrayList<InputModel>();
	public List<InputModel> getInputs() {
		return inputs;
	}
	private String elementName;
	@Override
	public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
		String eName = sName;
		InputModel input = null;
		if ("".equals(eName))
			eName = qName;
		elementName = eName;
		if(elementName.equals("input")){
			input = new InputModel();
			if (attrs != null) {
				for (int i = 0; i < attrs.getLength(); i++) {
					String atrName = attrs.getQName(i);
					if(atrName.equals("name")){
						input.setName(attrs.getValue(i));
					}if(atrName.equals("required")){
						input.setRequired(attrs.getValue(i));
					}if(atrName.equals("type")){
						input.setType(attrs.getValue(i));
					}
									
						
				}
			}
			inputs.add(input);
		}
		
	}
}
