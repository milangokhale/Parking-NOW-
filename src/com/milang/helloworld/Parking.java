package com.milang.helloworld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.milang.location.LocationFinder;
import com.milang.location.LocationFinder.LocationResult;

import com.milang.location.LocationUtil;
import com.milang.util.NumberFormat;

public class Parking extends Activity 
{	
	TextView textview_parking_address;
	TextView textview_distance_from_current_location;
	
	LocationFinder my_location_finder;
	

	private ListView listview_parking;
	
	ArrayList<String> my_address_array = new ArrayList<String>();
	
	ArrayList<SearchResults> array_search_results = new ArrayList<SearchResults>();

	
	public LocationResult my_location_result = new LocationResult(){

	    ArrayList<GeoPoint> my_geo_point_array = new ArrayList<GeoPoint>();
	    ArrayList<String> my_address_array = new ArrayList<String>();
		
		@Override
		public void gotLocation(Location location) {
			
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
				fillGeoPointsFromXml(myCurrentLocation);
				
				printClosestPoint(myCurrentLocation);
				
		        Collections.sort(array_search_results, new Comparator<Object>(){
		      	   
		            public int compare(Object o1, Object o2) {
		         	   SearchResults p1 = (SearchResults) o1;
		         	   SearchResults p2 = (SearchResults) o2;
		         	   
		         	   return p1.getCalcDistance().compareTo(p2.getCalcDistance());
		            }
		        });
		        
		        ArrayList<SearchResults> my_top_five = new ArrayList<SearchResults>();
		        
		        // Show top 10 results
		        for (int i=0; i < 10; i++) {
		        	
		        	my_top_five.add(array_search_results.get(i));
		        }
		        
		        final ListView lv1 = (ListView) findViewById(R.id.ListView01);
		        lv1.setAdapter(new MyCustomBaseAdapter(getBaseContext(), my_top_five));
		       
		        lv1.setOnItemClickListener(new OnItemClickListener() {
		        	
			         public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				          Object o = lv1.getItemAtPosition(position);
				          SearchResults fullObject = (SearchResults)o;
				          Toast.makeText(getBaseContext(), 
				        		  "You have chosen: " + " " + fullObject.getAddress(), Toast.LENGTH_LONG).show();
		         	 }
		        });
		    }
			
			catch (IOException e) {
				e.printStackTrace();
			} 
			
			catch (XmlPullParserException e) {
				e.printStackTrace();
			}
		}
		
		private void printClosestPoint(GeoPoint myCurrentLocation) {
			
			Double calcDistance = 200.0;
	    	Double tempCalcDistance = 200.0;
	    	
	    	String parkingAddress = "";
	    	
	    	ArrayList<Double> distanceArray = new ArrayList<Double>();
	    	
	    	// Find nearest parking location to my current location
	    	for (int i=0; i < my_geo_point_array.size(); i++) {
	    		calcDistance = LocationUtil.calcDistanceInKm(my_geo_point_array.get(i), myCurrentLocation);
	    		
	    		//distanceArray.add(NumberFormat.roundNumTwoDecimals(calcDistance));
	    		//distanceArray.
	    		
	    		//Collections.sort(distanceArray);
	    		
	    		// If distance calculated is less than previous distance, address becomes new array
	    		if (calcDistance < tempCalcDistance) {
	    			tempCalcDistance = calcDistance;
	    			parkingAddress = my_address_array.get(i);
	    		}
	    		
	    		else {
	    			
	    			// Distance to nearest parking spot is more than 200 kilometers away
	    		}
	    	}
	    	
	    	//Double calcDistanceString = NumberFormat.roundNumTwoDecimals(tempCalcDistance);
	    	
	    	//textview_parking_address.setText(parkingAddress);
	        //textview_distance_from_current_location.setText(calcDistanceString.toString() + " km");
	    	
	    	//String[] quotes = getBaseContext().getResources().getStringArray(R.array.quotes);
	        
	        // By using setAdpater method in listview we an add string array in list.
		    //listview_parking.setAdapter(new ArrayAdapter<Double>(getBaseContext(),android.R.layout.simple_list_item_1, distanceArray));
		}

		private void fillGeoPointsFromXml(GeoPoint myCurrentLocation) throws IOException, XmlPullParserException {
			
			 Resources res = getBaseContext().getResources();
			 XmlResourceParser parser = res.getXml(R.xml.parkinglocations);
			 
			 while (parser.next() != XmlPullParser.END_DOCUMENT)
			 {
				 String tagName = parser.getName();
				 String address = null;
				 String latitude = null;
				 String longitude = null;
				 Boolean line = false;
				 
				 if ((tagName!=null) && tagName.equals("parkinglocation")) {
					 int size = parser.getAttributeCount();
					 for (int i=0; i < size; i++) {
						 String attrName = parser.getAttributeName(i);
						 String attrValue = parser.getAttributeValue(i);
						 					 
						 if ((attrName != null) && attrName.equals("latitude")) {
							 latitude = attrValue;	
							 //line = false;
						 }
						 
						 else if ((attrName != null) && attrName.equals("longitude")) {
							 longitude = attrValue;
							 //line = false;
							 line = true;
						 }
						 
						 else if ((attrName !=null) && attrName.equals("address")){
							 address = attrValue;
						 }
						 
						 else {
							 latitude = null;
							 longitude = null;
						 }
						 
						 if ((latitude!=null)&& (longitude!=null)&&(line=true)) {
							 GeoPoint myGeoPoint = LocationUtil.getGeoPointFromStrings(latitude, longitude);
							 my_geo_point_array.add(myGeoPoint);
							 my_address_array.add(address);
							 line = false;
							 
							 Double calcDistance = 200.0;
							 calcDistance = LocationUtil.calcDistanceInKm(myGeoPoint, myCurrentLocation);
							 
							 SearchResults sr1 = new SearchResults();
						     sr1.setAddress(address);
						     sr1.setDistance("Distance: " + NumberFormat.roundNumber(calcDistance,2) + " km");
						     //sr1.setAvailability("Availability: " + NumberFormat.roundNumber(Math.random()*100,1) + "%");
						     sr1.setCalcDistance(calcDistance);
						     array_search_results.add(sr1);
						 }
					 }
				 }
			 }
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);       
        
        //textview_parking_address = (TextView) findViewById(R.id.TextViewParkingAddress);
        //textview_distance_from_current_location = (TextView) findViewById(R.id.TextViewDistance);
        
        //listview_parking=(ListView)findViewById(R.id.ListView_Parking);
    	
        my_location_finder = new LocationFinder();
        
        my_location_finder.getCurrentLocation(getBaseContext(), my_location_result);
        
        new ShowProgressDialog().execute();
        
        //ArrayList<SearchResults> searchResults = GetSearchResults();

    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.parkingmenu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.maps:
	    	showMap();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void showMap() {
		
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(this, MapMain.class));
		startActivity(intent);
	}
	
	public class ShowProgressDialog extends AsyncTask<Void, Integer, Void> {
		
		private final ProgressDialog dialog = new ProgressDialog(Parking.this);

        protected void onPreExecute()
        {
            this.dialog.setMessage(getString(R.string.msg_finding_location));
            this.dialog.show();
        }
		
	     protected Void doInBackground(Void... unused) {
	    	 
	    	//Wait 10 seconds to see if we can get a location from either network or GPS, otherwise stop
	            Long t = Calendar.getInstance().getTimeInMillis();
	            while (!my_location_finder.isLocationFound() && Calendar.getInstance().getTimeInMillis() - t < 15000) {
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