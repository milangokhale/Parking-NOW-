/**
 * 
 */
package com.milang.helloworld;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.google.android.maps.GeoPoint;
import com.milang.location.LocationUtil;

/**
 * @author milang
 *
 */
public class ParkingHelper {

	 /**
	 * Gets all parking locations from an xml file.
	 * 
	 * @param myCurrentLocation A GeoPoint which represents the current location.
	 * @param context The Context where this activity is used
	 * @return An array of search results with their calculated distances
	 */
	public static ArrayList<SearchResults> GetAllParkingLocations(GeoPoint myCurrentLocation, Context context) throws IOException, XmlPullParserException {
		
		 Resources res = context.getResources();
		 XmlResourceParser parser = res.getXml(R.xml.parkinglocations);
		 
		 ArrayList<SearchResults> array_search_results = new ArrayList<SearchResults>();
		 
		 while (parser.next() != XmlPullParser.END_DOCUMENT)
		 {
			 String tagName = parser.getName();
			 String address = null;
			 String latitude = null;
			 String longitude = null;
			 
			 if ((tagName!=null) && tagName.equals("parkinglocation")) {
				 int size = parser.getAttributeCount();
				 for (int i=0; i < size; i++) {
					 String attrName = parser.getAttributeName(i);
					 String attrValue = parser.getAttributeValue(i);
					 					 
					 if ((attrName != null) && attrName.equals("latitude")) {
						 latitude = attrValue;	
					 }
					 
					 else if ((attrName != null) && attrName.equals("longitude")) {
						 longitude = attrValue;
					 }
					 
					 else if ((attrName !=null) && attrName.equals("address")){
						 address = attrValue;
					 }
					 
					 else {
						 latitude = null;						 
						 longitude = null;
					 }
					 
					 if ((latitude!=null)&& (longitude!=null)) {
							
						// calculate distance for the point
						Double distance_from_current_loc = calcDistance(latitude, longitude, myCurrentLocation);
						
						 // add to search results array
						SearchResults sr1 = new SearchResults();
					    sr1.setAddress(address);
					    sr1.setCalcDistance(distance_from_current_loc);
						array_search_results.add(sr1);
					 }
				 }
			 }
		 }
		 
		 return array_search_results;
	}
	
	private static Double calcDistance(String latitude, String longitude, GeoPoint myCurrentLocation){
		
		return (Double)LocationUtil.calcDistanceInKm(latitude, longitude, myCurrentLocation); 
	}
}