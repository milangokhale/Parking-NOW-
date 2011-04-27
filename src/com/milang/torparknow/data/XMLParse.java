/**
 * 
 */
package com.milang.torparknow.data;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;


/**
 * @author milang
 * 
 */
public class XMLParse {
	
	public String mainTag = "";
	public String innerTagName = "";
	
	public ArrayList<String> myArray;
	
	/**
	 * @return the mainTag
	 */
	public String getMainTag() {
		return mainTag;
	}

	/**
	 * @param mainTag the mainTag to set
	 */
	public void setMainTag(String mainTag) {
		this.mainTag = mainTag;
	}

	/**
	 * @return the innerTagName
	 */
	public String getInnerTagName() {
		return innerTagName;
	}

	/**
	 * @param innerTagName the innerTagName to set
	 */
	public void setInnerTagName(String innerTagName) {
		this.innerTagName = innerTagName;
	}

	public ArrayList<String> FillArrayFromXml(XmlResourceParser parser) throws IOException, XmlPullParserException {
		 
		myArray = new ArrayList<String>();
		
		 while (parser.next() != XmlPullParser.END_DOCUMENT)
		 {
			 String tagName = parser.getName();
			 String innerTag = null;
			 
			 if ((tagName!=null) && tagName.equals(mainTag)) {
				 int size = parser.getAttributeCount();
				 for (int i=0; i < size; i++) {
					 String attrName = parser.getAttributeName(i);
					 String attrValue = parser.getAttributeValue(i);
					 					 
					 if ((attrName != null) && attrName.equals(innerTagName)) {
						 innerTag = attrValue;					 
					 }
					 
					 // Create an array from string
					 if (innerTag!=null){						 
						 myArray.add(innerTag);						 
					 }
				 }
			 }
		 }
		 
		 return myArray;
	}
}
