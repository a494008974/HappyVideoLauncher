package com.mylove.happyvideo.parser;

import java.io.InputStream;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class DesktopParser {
	private final static String TAG = DesktopParser.class.getSimpleName();
	private String httpMessage;
	
	private String desktop;
	
	public String getDesktop() {
		return desktop;
	}

	public void setDesktop(String desktop) {
		this.desktop = desktop;
	}

	public DesktopParser(String httpMessage) {
		this.httpMessage = httpMessage;
		StringReader reader = new StringReader(httpMessage);
		parser(reader);
	}
	

	public void parser(StringReader reader) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(reader);
			
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("urllist".equalsIgnoreCase(parser.getName())) {
						int j = parser.getAttributeCount();
						for (int k = 0; k < j; k++) {
							if ("key".equalsIgnoreCase(parser
									.getAttributeName(k))) {
								String str = parser.getAttributeValue(k);
								if("desktop".equalsIgnoreCase(str)){
									
									setDesktop(parser.nextText().trim());
								}
								break;
							}
						}
					}
					break;
				case XmlPullParser.END_TAG:

					break;
				}
				eventType = parser.next();
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

	}

}
