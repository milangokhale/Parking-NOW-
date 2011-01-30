package com.milang.helloworld.data;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class TwitterXMLParse extends DefaultHandler {

    StringBuilder sb = null;
    String ret = "";
    boolean bStore = false;
    int howMany = 0;

    TwitterXMLParse() {
    }

    String getResults()
    {
        return "XML parsed data.\nThere are [" + howMany + "] status updates\n\n" + ret;
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
            if (localName.equals("status")) {
                this.sb = new StringBuilder("");
                bStore = true;
            }
            if (localName.equals("user")) {
                bStore = false;
            }
            if (localName.equals("text")) {
                this.sb = new StringBuilder("");
            }
            if (localName.equals("created_at")) {
                this.sb = new StringBuilder("");
            }
        } catch (Exception ee) {

            Log.d("error in startElement", ee.getStackTrace().toString());
        }
    }

    @Override

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (bStore) {
            if (localName.equals("created_at")) {

                ret += "Date: " + sb.toString() + "\n"; 
                sb = new StringBuilder("");
                return;
            }

            if (localName.equals("user")) {
                bStore = true;
            }

            if (localName.equals("text")) {

                ret += "Post: " + sb.toString() + "\n\n";
                sb = new StringBuilder("");
                return;

            }


        }
        if (localName.equals("status")) {
            howMany++;
            bStore = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {

        if (bStore) {
            String theString = new String(ch, start, length);

            this.sb.append(theString);
        }
    }

}

