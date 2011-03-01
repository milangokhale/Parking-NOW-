package com.milang.location;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

/**
 * @author milang
 *
 * LocationUtil is a utility class to calculate common location-based functions.
 */
public class LocationUtil 
{
	private final static int EARTH_RADIUS_KM = 6371;
    private final static int MILLION = 1000000;
    

    /**
	 * Displays a toast with the GPS coordinates of a given Location.  
	 * 
	 * @param context Given context that is used for the toast.  Use getBaseContext() if needed.
	 * @param location Location object.
	 */
	public static void displayLocationText(Context context, Location location){
		
		if (location!=null) {
			Toast.makeText(context, "Latitude: " + location.getLatitude() + 
					" Longitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();
		}
	}
	
    /**
	 * Get GeoPoint from latitude/longitude pair of strings. 
	 * 
	 * @param lat Latitude of point.
	 * @param lon Longitude of point.
	 * @return A GeoPoint with given latitude and longitude coordinates.
	 */
	public static GeoPoint getGeoPointFromStrings(String lat, String lon) {
		
		Double x = Double.parseDouble(lat);
		Double y = Double.parseDouble(lon);
		
		return(new GeoPoint((int)(x*MILLION),(int)(y*MILLION)));
	}
	
    /**
	 * Get GeoPoint from latitude/longitude pair of floats. 
	 * 
	 * @param lat Latitude of point.
	 * @param lon Longitude of point.
	 * @return A GeoPoint with given latitude and longitude coordinates.
	 */
	public static GeoPoint getGeoPointFromFloats(float lat, float lon) {

		return(new GeoPoint((int)(lat*MILLION),(int)(lat*MILLION)));
	}
	
    /**
	 * Get GeoPoint from a Location object.
	 * 
	 * @param location A Location object to be parsed. 
	 * @return A GeoPoint with given latitude and longitude coordinates.
	 */
	public static GeoPoint getGeoPointFromLocation(Location location) {
		
		double x = location.getLatitude();
		double y = location.getLongitude();
		
		return(new GeoPoint((int)(x*MILLION),(int)(y*MILLION)));
	}
    
	/**
	 * Computes the distance in kilometers between two points on Earth.
	 * 
	 * @param lat1 Latitude of the first point
	 * @param lon1 Longitude of the first point
	 * @param lat2 Latitude of the second point
	 * @param lon2 Longitude of the second point
	 * @return Distance between the two points in kilometers.
	 */
	public static double calcDistanceInKm(double lat1, double lon1, double lat2, double lon2) {
	    
		// lat1/180*pi
		double lat1Rad = Math.toRadians(lat1);
		
		// lat2/180*pi
	    double lat2Rad = Math.toRadians(lat2);
	    
	    //lon2-lon1/180*pi
	    double deltaLonRad = Math.toRadians(lon2 - lon1);
	    
	    return Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad)
	            * Math.cos(deltaLonRad))
	            * EARTH_RADIUS_KM;
	}
	
	 /**
	 * Computes the distance in kilometers between two points on Earth.
	 * 
	 * @param p1 First point
	 * @param p2 Second point
	 * @return Distance between the two points in kilometers.
	 */
	public static double calcDistanceInKm(GeoPoint p1, GeoPoint p2) {
	    double lat1 = p1.getLatitudeE6() / (double)MILLION;
	    double lon1 = p1.getLongitudeE6() / (double)MILLION;
	    double lat2 = p2.getLatitudeE6() / (double)MILLION;
	    double lon2 = p2.getLongitudeE6() / (double)MILLION;
	
	    return calcDistanceInKm(lat1, lon1, lat2, lon2);
	}
	
	 /**
	 * Computes the distance in kilometers between two points on Earth.
	 * 
	 * @param lat Latitude of the first point
	 * @param lon Longitude of the first point
	 * @param p2 Second point
	 * @return Distance between the two points in kilometers.
	 */
	public static double calcDistanceInKm(String lat, String lon, GeoPoint p2) {

		GeoPoint p1 = LocationUtil.getGeoPointFromStrings(lat, lon);
		return calcDistanceInKm(p1, p2);
	}
	
	 /**
	 * Computes the distance in kilometers between two points on Earth.
	 * 
	 * @param lat Latitude of the first point
	 * @param lon Longitude of the first point
	 * @param p2 Second point
	 * @return Distance between the two points in kilometers.
	 */
	public static double calcDistanceInKm(float lat, float lon, GeoPoint p2) {

		GeoPoint p1 = LocationUtil.getGeoPointFromFloats(lat, lon);
		return calcDistanceInKm(p1, p2);
	}
}