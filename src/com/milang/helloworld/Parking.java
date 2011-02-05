package com.milang.helloworld;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Parking extends Activity 
{	
	private final static int EARTH_RADIUS_KM = 6371;
    public final static int MILLION = 1000000;
    
    MapController myMapController;
    MapView myMapView;
    
    TextView textViewParkingAddress;
    TextView textViewDistance;
    
    ArrayList<GeoPoint> myGeoPointArray = new ArrayList<GeoPoint>();
    ArrayList<String> myAddressArray = new ArrayList<String>();
    
    LocationManager locationManager;

    LocationListener locationListener;
    GeoPoint myCurrentLocation;
    

	boolean hasLocation = false; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking);       
        
        textViewParkingAddress = (TextView) findViewById(R.id.TextViewParkingAddress);
        textViewDistance = (TextView) findViewById(R.id.TextViewDistance);
        
        //getCurrentLocation();
        GeoPoint tempLocation = getPoint("43.80485","-79.31033");
        FillData(tempLocation);
    }
	
	private void getCurrentLocation() {
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		myCurrentLocation = new GeoPoint((int) (lastKnownLocation.getLatitude() * 1E6), (int) (lastKnownLocation.getLongitude() * 1E6));
        
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
            	
              // Called when a new location is found by the network location provider.
            	if (location != null) {
            		myCurrentLocation = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
            	}
            	
            	//Toast.makeText(getBaseContext(),
            	          //"Latitude: " + location.getLatitude() + 
            	          //" Longitude: " + location.getLongitude(), 
            	          //Toast.LENGTH_SHORT).show();
            	
            	hasLocation = true;
            	
            	FillData(myCurrentLocation);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
          };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 10000, locationListener);
		
        new GetLocationTask().execute();
	}
	
	public void onStop(){
    	super.onStop();
    	locationManager.removeUpdates(locationListener);
    }
	
	private void FillData(GeoPoint myCurrentLocation) {
		try {
			// Get all GeoPoints from xml
			FillGeoPointsFromXml();
			
	    	Double calcDistance = 10000.0;
	    	Double tempCalcDistance = 10000.0;
	    	
	    	String parkingAddress = "";
	    	
	    	// Find nearest parking location to my current location
	    	for (int i=0; i < myGeoPointArray.size(); i++) {
	    		calcDistance = distanceKm(myGeoPointArray.get(i), myCurrentLocation);
	    		
	    		// If distance calculated is less than previous distance, address becomes new array
	    		if (calcDistance < tempCalcDistance) {
	    			tempCalcDistance = calcDistance;
	    			parkingAddress = myAddressArray.get(i);
	    		}
	    		
	    		else {
	    			
	    			// Distance to nearest parking spot is more than 1000 kilometers away
	    		}
	    	}
	    	
	    	Double calcDistanceString = roundTwoDecimals(tempCalcDistance);
	    	
	        textView.setText("You should park at: " +
	        		parkingAddress  + ". It is " + calcDistanceString + " kilometers away!");
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	/**
     * Rounds an input double number to two decimal places.
     * 
     * @param num Number that is to be shortened to two decimal places.
     * @return A double number to two decimal places.
     */	
	private double roundTwoDecimals(double num) 
	{
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(num));
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
    
	public class GetLocationTask extends AsyncTask<Void, Integer, Void> {
		
		private final ProgressDialog dialog = new ProgressDialog(Parking.this);

        protected void onPreExecute()
        {
            this.dialog.setMessage("Finding current location...");
            this.dialog.show();
        }
		
	     protected Void doInBackground(Void... unused) {
	    	 
	    	//Wait 10 seconds to see if we can get a location from either network or GPS, otherwise stop
	            Long t = Calendar.getInstance().getTimeInMillis();
	            while (!hasLocation && Calendar.getInstance().getTimeInMillis() - t < 15000) {
	                try {
	                    Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            };
	    	 
	    	 return(null);
	     }
	     
	     protected void onPostExecute(final Void unused)
	        {
	            if(this.dialog.isShowing())
	            {
	                this.dialog.dismiss();
	            }
	            
	            else
	            {
	                //Couldn't find location, do something like show an alert dialog
	            }
	        }
	}
}