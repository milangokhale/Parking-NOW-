/**
 * 
 */
package com.milang.torparknow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.maps.GeoPoint;
import com.milang.location.LocationFinder;
import com.milang.location.LocationUtil;
import com.milang.location.LocationFinder.LocationResult;

/**
 * @author milang
 *
 */
public class MyLocationResult extends LocationResult {
	
	

	/* (non-Javadoc)
	 * @see com.milang.location.LocationFinder.LocationResult#gotLocation(android.location.Location)
	 */
	@Override
	public void gotLocation(Location location) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isNewLocationFound() {
		
		return true; 
		//my_location_finder.isLocationFound(); 
	}
	
	/*
	private void lksdjafljd(LocationFinder my_location_finder){
		
		if (isNewLocationFound()) {
			//ProgressBar dialog = (ProgressBar)findViewById(R.id.progressBar1);
			//dialog.setVisibility(View.INVISIBLE);
			
			//my_image_view = (ImageView)findViewById(R.id.refresh_icon);
			//my_image_view.setImageResource(R.drawable.refresh);
			//my_image_view.setVisibility(View.VISIBLE);
		}
		
		GeoPoint current_location;
		
		// if there is no provider on the phone, throw message
		if ((location==null) && (!isEmulator)) {
			Toast.makeText(getBaseContext(), "@string/msg_location_not_found", Toast.LENGTH_LONG);
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
			
			try {
				// Get all parking location address names and their calculated distances
				ArrayList<CarparkNow> array_car_park = ParkingHelper.loadCarparkNow(getBaseContext(), current_location);
				
				// Sort all parking locations by proximity to current location
		        Collections.sort(array_car_park, new Comparator<Object>(){
		      	   
		            public int compare(Object o1, Object o2) {
		              CarparkNow p1 = (CarparkNow) o1;
		              CarparkNow p2 = (CarparkNow) o2;
		         	  
		         	  return p1.getCalcDistance().compareTo(p2.getCalcDistance());
		            }
		        });
		        
		        final ArrayList<CarparkNow> array_search_results_short_list = new ArrayList<CarparkNow>();
		        
		        int maxNumOfResults = Integer.parseInt(max_num_of_results);
		        
		        // Show top n results only
		        for (int i=0; i < maxNumOfResults; i++) {
		        	array_search_results_short_list.add(array_car_park.get(i));
		        }
		        
		        // Add the search results to the list view
		        ListView lv1 = (ListView) findViewById(R.id.ListView01);
		        lv1.setAdapter(new MyCustomBaseAdapter(getBaseContext(), array_search_results_short_list));
		        
		        lv1.setOnItemClickListener(new OnItemClickListener() {
		        	
			         public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			        	 
			        	Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri
			     				.parse("google.navigation:q=" + array_search_results_short_list.get(position).getStreetAddress()));
			     		startActivity(intent);
		         	 }
		        });
		        
		        // Store the search results so that they can be accessed by the map
		        storeSearchResults(array_search_results_short_list, maxNumOfResults);
		    }
			
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}

*/
}
