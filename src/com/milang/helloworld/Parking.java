package com.milang.helloworld;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;



public class Parking extends Activity 
{	
	private final static int EARTH_RADIUS_KM = 6371;
    public final static int MILLION = 1000000;
    
    MapController myMapController;
    MapView myMapView;
    
    LocationListener locationListener;
    
    TextView textView;
    
    ArrayList<GeoPoint> myGeoPointArray = new ArrayList<GeoPoint>();
    ArrayList<String> myAddressArray = new ArrayList<String>();
    
    LocationManager locationManager;
    
    GeoPoint myCurrentLocation;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking);       
        
        textView = (TextView) findViewById(R.id.Parking);
        	
        
        // For each GeoPoint, calculate the distance between them and add to an array
        // for (int i=0; i< length of file, i++) 
        
		try {

	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        
	       // Coordinates for Ritika's place
	        Double lat = 43.65419*1E6;
	        Double lng = -79.37595*1E6;  
	        
	        // Create a new GeoPoint object and add it to the OverlayItem object
	        //myCurrentLocation = new GeoPoint(lat.intValue(),lng.intValue());        
	        
    		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    		myCurrentLocation = new GeoPoint((int) (lastKnownLocation.getLatitude() * 1E6), (int) (lastKnownLocation.getLongitude() * 1E6));
	        
	        // Define a listener that responds to location updates
	        locationListener = new LocationListener() {
	            public void onLocationChanged(Location location) {
	              // Called when a new location is found by the network location provider.
	            	if (location == null) {
	            		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	            		myCurrentLocation = new GeoPoint((int) (lastKnownLocation.getLatitude() * 1E6), (int) (lastKnownLocation.getLongitude() * 1E6));
	            	}
	            	
	            	else {
	            	      myCurrentLocation = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
	            	}
	            	
	            	Toast.makeText(getBaseContext(),
	            	          "Latitude: " + location.getLatitude() + 
	            	          " Longitude: " + location.getLongitude(), 
	            	          Toast.LENGTH_SHORT).show();
	            }

	            public void onStatusChanged(String provider, int status, Bundle extras) {}

	            public void onProviderEnabled(String provider) {}

	            public void onProviderDisabled(String provider) {}
	          };

	        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, locationListener);
			
			// Get all GeoPoints from xml
			FillGeoPointsFromXml();
			
			// Nina's place location
	        //Double myLatitude = 43.66586*1E6;
	        //Double myLongitude = -79.38736*1E6;
	    	//GeoPoint myLocation = new GeoPoint(myLatitude.intValue(),myLongitude.intValue());
	    	
	    	Double calcDistance = 10000.0;
	    	Double tempCalcDistance = 10000.0;
	    	
	    	//GeoPoint closestGeoPoint;
	    	//String x = "";
	    	//String y = "";
	    	String tempAddress = "";
	    	
	    	// Find nearest parking location to my current location
	    	for (int i=0; i < myGeoPointArray.size(); i++) {
	    		calcDistance = distanceKm(myGeoPointArray.get(i),myCurrentLocation);
	    		if (calcDistance < tempCalcDistance ) {
	    			tempCalcDistance = calcDistance;
	    			//closestGeoPoint = myGeoPointArray.get(i);
	    	    	//x = Integer.toString(closestGeoPoint.getLatitudeE6());
	    	    	//y = Integer.toString(closestGeoPoint.getLongitudeE6());
	    	    	
	    	    	//address = GetAddress(closestGeoPoint);
	    	    	
	    	    	tempAddress = myAddressArray.get(i);
	    		}
	    	}    	
	    	
	    	Double calcDistanceString = roundTwoDecimals(calcDistance);
	    	
	        textView.setText(tempAddress + ". It is " + calcDistanceString + " kilometers away!");
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private void UpdateTextView() {
		
	}
	
	private void FillGeoPointsFromXml() throws IOException, XmlPullParserException {
		
		 Resources res = this.getResources();
		 XmlResourceParser parser = res.getXml(R.xml.parkinglocations);
		 
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
					 
					 if ((latitude!=null)&& (longitude!=null)) {
						 GeoPoint myGeoPoint = getPoint(latitude, longitude);
						 myGeoPointArray.add(myGeoPoint);
						 myAddressArray.add(address);
					 }
				 }
			 }
		 }
	}
	
	private GeoPoint getPoint(String lat, String lon) {
		
		Double x = Double.parseDouble(lat);
		Double y = Double.parseDouble(lon);
		
		return(new GeoPoint((int)(x*MILLION),(int)(y*MILLION)));
	}
	

		private String getEventsFromAnXML(Activity activity) throws XmlPullParserException, IOException
		{
			
		 StringBuffer stringBuffer = new StringBuffer();
		 Resources res = activity.getResources();
		 XmlResourceParser parser = res.getXml(R.xml.parkinglocations);
		 
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
					 
					 if ((attrName!=null) && attrName.equals("address")) {
						 address = attrValue;
					 }
					 
					 else if ((attrName != null) && attrName.equals("latitude")) {
						 latitude = attrValue;					 
					 }
					 
					 else if ((attrName != null) && attrName.equals("longitude")) {
						 longitude = attrValue;					 
					 }
				 }
				 
				 if(address != null){
					 stringBuffer.append("Address: " + address + "\n");
					 stringBuffer.append("Latitude: " + latitude + "\n");
					 stringBuffer.append("Longitude: " + longitude + "\n");
				 }
			 }
		 }
		 
		 return stringBuffer.toString();
		}
	
	private String GetAddress(GeoPoint gp){

		String add = "";
		
    	Geocoder geoCoder = new Geocoder(
                getBaseContext(), Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(
                    gp.getLatitudeE6()  / 1E6, 
                    gp.getLongitudeE6() / 1E6, 1);

                
                if (addresses.size() > 0) 
                {
                    for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
                         i++)
                       add += addresses.get(0).getAddressLine(i) + "\n";
                }

                //Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {                
                e.printStackTrace();
            }   

        return add;
	}
	
	private double roundTwoDecimals(double d) 
	{
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
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
    public static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        
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
    public static double distanceKm(GeoPoint p1, GeoPoint p2) {
        double lat1 = p1.getLatitudeE6() / (double)MILLION;
        double lon1 = p1.getLongitudeE6() / (double)MILLION;
        double lat2 = p2.getLatitudeE6() / (double)MILLION;
        double lon2 = p2.getLongitudeE6() / (double)MILLION;

        return distanceKm(lat1, lon1, lat2, lon2);
    }
}