/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SerializePhraseHandler.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-3-4 下午09:36:09
 *******************************************************************************/
package com.funoble.myarstation.phrase;

import java.io.OutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;


public class SerializePhraseHandler {
	public static void save(List<Phrase> phrases, OutputStream outputStream) throws Throwable {
		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(outputStream, "UTF-8");
		serializer.startDocument("UTF-8", true);
		
		serializer.startTag(null, "Phrases");
		for(Phrase phrase : phrases) {
			serializer.startTag(null, "item");
			serializer.attribute(null, "edit", phrase.getEdit().toString());
			serializer.text(phrase.getWord());
			serializer.endTag(null, "item");
		}
		serializer.endTag(null, "Phrases");
		serializer.endDocument();
		outputStream.flush();
		outputStream.close();
	}
}
