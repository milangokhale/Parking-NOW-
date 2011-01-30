package com.milang.helloworld.data;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XMLHandler extends DefaultHandler {

    StringBuilder sb = null;
    String ret = "";
    boolean bAddress = false;
    boolean bLatitude = false;
    boolean bLongitude = false;

    // Default constructor
    public XMLHandler() {
    }

    @Override
    public void startDocument() throws SAXException {
        // initialize "list"
    }

    @Override
    public void endDocument() throws SAXException {
    	

    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, 
Attributes atts) throws SAXException {

        try {
            if (localName.equalsIgnoreCase("address")) {
            	this.sb = new StringBuilder("");
                bAddress = true;
            }
        }
        
        catch (Exception ex) {
            Log.d("error in startElement", ex.getStackTrace().toString());
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    	
    	if (localName.equals("address")) {
    		ret += "Address: " + sb.toString() + "\n"; 
            sb = new StringBuilder("");
            return;
      }    	
    }

    @Override
    public void characters(char ch[], int start, int length) {

    	if (bAddress) {
            String theString = new String(ch, start, length);
            this.sb.append(theString);
        }
    }
}

