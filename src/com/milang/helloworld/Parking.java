package com.milang.helloworld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.milang.location.LocationFinder;
import com.milang.location.LocationFinder.LocationResult;

import com.milang.location.LocationUtil;


public class Parking extends Activity 
{	
	public LocationFinder my_location_finder;
	//private final static int MAX_TIME_GPS = 5000;
	private final static int MAX_NUM_OF_RESULTS = 5;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);       
    	
        my_location_finder = new LocationFinder();
        my_location_finder.getCurrentLocation(getBaseContext(), my_location_result);
        
        //new ShowProgressDialog().execute();
    }
	
	public LocationResult my_location_result = new LocationResult(){
		
		//private Carpark carpark = null;
		//private static final String CARPARK_INDEX = "carpark_index";
		
		@Override
		public void gotLocation(Location location) {
			
			final GeoPoint current_location;
			
			// if run on emulator, use a hard-coded location - my house!
			if ("google_sdk".equals(android.os.Build.PRODUCT)){ 
				current_location = LocationUtil.getGeoPointFromStrings("43.8049", "-79.3105");
			}
			
			else {
				current_location = LocationUtil.getGeoPointFromLocation(location); 
				}
			
			/*
			else 
			{
				if (location.equals(null)){ 
					Toast.makeText(getBaseContext(), "Your location could not be found! Make sure your network or GPS locations ervices are turned on.", Toast.LENGTH_LONG);
					//current_location = LocationUtil.getGeoPointFromStrings("43.8049", "-79.3105");
					
					}
			}
			
			*/
			
			try {
				// Get all parking location address names and their calculated distances
				//ArrayList<SearchResults> array_search_results = ParkingHelper.GetAllParkingLocations(current_location, getBaseContext());
				
				
				ArrayList<Carpark> array_car_park = GreenParkingApp.cachedCarparks(getBaseContext());
				
				
				// Sort all parking locations by proximity to current location
		        Collections.sort(array_car_park, new Comparator<Object>(){
		      	   
		            public int compare(Object o1, Object o2) {
		         	  Carpark p1 = (Carpark) o1;
		         	  Carpark p2 = (Carpark) o2;
		         	  
		         	  Double p1_dist_from_current_location = LocationUtil.calcDistanceInKm(p1.getLat(), p1.getLng(), current_location);
		         	  Double p2_dist_from_current_location = LocationUtil.calcDistanceInKm(p2.getLat(), p2.getLng(), current_location);
		         	   
		         	   return p1_dist_from_current_location.compareTo(p2_dist_from_current_location);
		            }
		        });
		        
		        final ArrayList<Carpark> array_search_results_short_list = new ArrayList<Carpark>();
				
		        
		        // Show top n results only
		        for (int i=0; i < MAX_NUM_OF_RESULTS; i++) {
		        	array_search_results_short_list.add(array_car_park.get(i));
		        }
				
				final ListView lv1 = (ListView) findViewById(R.id.ListView01);
		        lv1.setAdapter(new MyCustomBaseAdapter(getBaseContext(), array_search_results_short_list));
		       
		        lv1.setOnItemClickListener(new OnItemClickListener() {
		        	
			         public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			        	 
			        	Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri
			     				.parse("google.navigation:q=" + array_search_results_short_list.get(position).directionsAddress()));
			     		startActivity(intent);
				          
		         	 }
		        });
		    }
			
			catch (Exception ex) {
				ex.printStackTrace();
			} 
			
			
		}
		
		//private void 
		
		
	};

	
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
	
	/*
	 * Not using this code anymore - it was showing a progress dialog.
	 * 
	public class ShowProgressDialog extends AsyncTask<Void, Integer, Void> {
		
		private final ProgressDialog dialog = new ProgressDialog(Parking.this);

        protected void onPreExecute()
        {
            this.dialog.setMessage(getString(R.string.msg_finding_location));
            this.dialog.show();
        }
		
	     protected Void doInBackground(Void... unused) {
	    	 
	    	//Wait to see if we can get a location from either network or GPS, otherwise stop
	            Long t = Calendar.getInstance().getTimeInMillis();
	            while (!my_location_finder.isLocationFound() && Calendar.getInstance().getTimeInMillis() - t < MAX_TIME_GPS) {
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
	*/
}