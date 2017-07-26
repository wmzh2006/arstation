/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: saxperson.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-3-4 下午06:17:20
 *******************************************************************************/
package com.funoble.myarstation.phrase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SaxPhrase {
	public List<Phrase> getPersons(InputStream stream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		PersonHandler handler = new PersonHandler();
		parser.parse(stream, handler);
		return handler.getPersons();
	}
	
	private class PersonHandler extends DefaultHandler {
		List<Phrase> phrases = null;
		Phrase phrase = null;
		String tag = null;
		/* 
		 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
		 */
		@Override
		public void startDocument() throws SAXException {
			phrases = new ArrayList<Phrase>();
		}

		/* 
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if("item".equals(localName)) {
				phrase = new Phrase();
				phrase.setEdit(Boolean.valueOf(attributes.getValue(0)));
			}
			tag = localName;
		}

		/* 
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if("item".equals(localName)) {
				phrases.add(phrase);
				phrase = null;
			}
			tag = null;
		}

		/* 
		 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
		 */
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if(tag != null) {
				if("item".equals(tag)) {
					String dataString = new String(ch, start, length);
					phrase.setWord(dataString);
				}
			}
		}

		/**
		 * return persons : return the property persons.
		 */
		public List<Phrase> getPersons() {
			return phrases;
		}
		
	}
}
