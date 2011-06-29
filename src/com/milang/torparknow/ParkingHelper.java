/**
 * 
 */
package com.milang.torparknow;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.android.maps.GeoPoint;
import com.milang.location.LocationUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * @author milang
 *
 */
public class ParkingHelper {
	
	// Variable declarations
	private static final boolean isEmulator = "google_sdk".equals(android.os.Build.PRODUCT);
	public final static String TAG = "locationNotFound";
	public static String MAX_NUM_OF_RESULTS;
	public static String DISPLAY_TYPE;
	

	public static void initSettings(Context context){
		
		// Load settings
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		MAX_NUM_OF_RESULTS = prefs.getString("pref_numOfResults","4");
		DISPLAY_TYPE = prefs.getString("pref_defaultdisplay","Map");
		
		//prefs.edit().putInt("pref_version", R.string.versionNum).commit();
	}
	
	/**
	 * Loads the car parks from JSON file.  
	 * 
	 * @param context Given context for the activity.  Use getBaseContext() if needed.
	 * @param currentLocation The current location that is used to calculate distance from parking lot.
	 */
	public static ArrayList<CarparkNow> loadCarparkNow(Context context, GeoPoint currentLocation){
		
		// Get all Green P lots from JSON
		ArrayList<Carpark> myCarparkArray = GreenParkingApp.cachedCarparks(context);

		ArrayList<CarparkNow> myCarparkNowArray = new ArrayList<CarparkNow>();
		
		for (int i =0; i < myCarparkArray.size(); i++){
			
			CarparkNow my_carpark_now = new CarparkNow();
			
			my_carpark_now.setCapacity(myCarparkArray.get(i).getCapacity());
			my_carpark_now.setRate(myCarparkArray.get(i).getRate());
			my_carpark_now.setStreetAddress(myCarparkArray.get(i).getStreetAddress());
			my_carpark_now.setLat(myCarparkArray.get(i).getLat());
			my_carpark_now.setLng(myCarparkArray.get(i).getLng());
			
			Double distance_from_lot = LocationUtil.calcDistanceInKm(myCarparkArray.get(i).getLat(), 
					myCarparkArray.get(i).getLng(), currentLocation);
			
			my_carpark_now.setCalcDistance(distance_from_lot);
			
			myCarparkNowArray.add(my_carpark_now);
		}
		
		return myCarparkNowArray;
	}
	
    /**
	 * Gets the nearest parking lots to the given location.  
	 * 
	 * @param context Given context for the activity.  Use getBaseContext() if needed.
	 * @param location The provided location to calculate the nearest parking lots.
	 * @param numOfResults The number of parking lots to be returned which are closest to the given location.
	 */
	public static ArrayList<CarparkNow> getNearestParkingLots(Context context, Location location, int numOfResults){
				
		GeoPoint current_location = null;
		
		// if there is no provider on the phone, throw message
		if ((location==null) && (!isEmulator)) {
			Log.d(TAG, "@string/msg_location_not_found");
		}
		
		else {
			if (isEmulator) {
				//Ritika's place: "43.654278", "-79.376202"
				//Nina's place: "43.66563", "-79.38750"
				//Darnborough: "43.80465", "-79.31049"
				current_location = LocationUtil.getGeoPointFromStrings("43.66563", "-79.38750");
			}
			
			else {
				current_location = LocationUtil.getGeoPointFromLocation(location);
			}
		}
	
		// Get all parking location address names and their calculated distances
		ArrayList<CarparkNow> array_car_park = loadCarparkNow(context, current_location);
		
		// Sort all parking locations by proximity to current location
        Collections.sort(array_car_park, new Comparator<Object>(){
      	   
            public int compare(Object o1, Object o2) {
              CarparkNow p1 = (CarparkNow) o1;
              CarparkNow p2 = (CarparkNow) o2;
         	  
         	  return p1.getCalcDistance().compareTo(p2.getCalcDistance());
            }
        });
        
        final ArrayList<CarparkNow> array_search_results_short_list = new ArrayList<CarparkNow>();
        
        // Short list the top n results only
        for (int i=0; i < numOfResults; i++) {
        	array_search_results_short_list.add(array_car_park.get(i));
        }
        
        return array_search_results_short_list;
	}
	
	public boolean isNewLocationFound() {
		
		return true; 
		//my_location_finder.isLocationFound(); 
	}

	/*
	public static void storeSearchResults(ArrayList<CarparkNow> array_search_results, int maxNumOfResults) {
        
		db.delete(DataHelper.TABLE_NAME, null, null);
		
        // Load data into db 
        // This will get used by the map if need be
        for (int i=0; i < maxNumOfResults; i++) {
        	float lat = array_search_results.get(i).getLat();
        	float lng = array_search_results.get(i).getLng();
        	insertRecord(lat, lng);
        }
	}
	
	private void insertRecord(float lng, float lat){		

		ContentValues cv = new ContentValues();
		cv.put(DataHelper.TITLE, lng);
		cv.put(DataHelper.VALUE, lat);
		
		db.insert(DataHelper.TABLE_NAME, DataHelper.TITLE, cv);
		//db.close();
	}
	
	*/
	
	

	 /**
	 * Gets all parking locations from an xml file.
	 * 
	 * @param myCurrentLocation A GeoPoint which represents the current location.
	 * @param context The Context where this activity is used
	 * @return An array of search results with their calculated distances
	 */
	
	/*
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
	
	*/
}