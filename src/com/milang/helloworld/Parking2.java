package com.milang.helloworld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.milang.location.LocationFinder;
import com.milang.location.LocationFinder.LocationResult;

import com.milang.location.LocationUtil;
import com.milang.util.NumberFormat;


public class Parking2 extends Activity 
{	
    
    TextView textViewParkingAddress;
    TextView textViewDistance;

    LocationManager locationManager;

    LocationListener locationListener;
    GeoPoint myCurrentLocation;

	boolean hasLocation = false; 
	
	
	LocationFinder my_location_finder;
	
	public LocationResult location_result = new LocationResult(){

	    ArrayList<GeoPoint> myGeoPointArray = new ArrayList<GeoPoint>();
	    ArrayList<String> myAddressArray = new ArrayList<String>();	    
		
		@Override
		public void gotLocation(Location location) {
			
			//LocationUtil.displayLocationText(getBaseContext(), location);
			
			FillData(location);
		}
		
		public void FillData(Location location) {
			
			GeoPoint found_geo_point = LocationUtil.getGeoPointFromLocation(location);
			FillData(found_geo_point);
			
			// using temp data
			//GeoPoint tempLocation = LocationUtil.getGeoPointFromStrings("43.80503", "-79.31040");
			//FillData(tempLocation);
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
		    		calcDistance = LocationUtil.calcDistanceInKm(myGeoPointArray.get(i), myCurrentLocation);
		    		
		    		// If distance calculated is less than previous distance, address becomes new array
		    		if (calcDistance < tempCalcDistance) {
		    			tempCalcDistance = calcDistance;
		    			parkingAddress = myAddressArray.get(i);
		    		}
		    		
		    		else {
		    			
		    			// Distance to nearest parking spot is more than 1000 kilometers away
		    		}
		    	}
		    	
		    	Double calcDistanceString = NumberFormat.roundNumTwoDecimals(tempCalcDistance);
		    	
		        textViewParkingAddress.setText(parkingAddress);
		        textViewDistance.setText(calcDistanceString.toString() + " km");
			}
			
			catch (IOException e) {
				e.printStackTrace();
			} 
			
			catch (XmlPullParserException e) {
				e.printStackTrace();
			}
		}
		
		private void FillGeoPointsFromXml() throws IOException, XmlPullParserException {
			
			 Resources res = getBaseContext().getResources();
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
							 GeoPoint myGeoPoint = LocationUtil.getGeoPointFromStrings(latitude, longitude);
							 myGeoPointArray.add(myGeoPoint);
							 myAddressArray.add(address);
						 }
					 }
				 }
			 }
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking);       
        
        textViewParkingAddress = (TextView) findViewById(R.id.TextViewParkingAddress);
        textViewDistance = (TextView) findViewById(R.id.TextViewDistance);
        
        my_location_finder = new LocationFinder();
        
        my_location_finder.getCurrentLocation(getBaseContext(), location_result);
    }
	    
	public class GetLocationTask extends AsyncTask<Void, Integer, Void> {
		
		private final ProgressDialog dialog = new ProgressDialog(Parking2.this);

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