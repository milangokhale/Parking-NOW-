package com.milang.torparknow;

import java.util.ArrayList;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

import com.milang.location.LocationFinder;
import com.milang.location.LocationFinder.LocationResult;


public class ParkingActivity extends Activity 
{	
	public LocationFinder my_location_finder;
	
	private final boolean isEmulator = "google_sdk".equals(android.os.Build.PRODUCT);
	ImageView my_image_view;
	//private SQLiteDatabase db;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview); 
    	
        my_location_finder = new LocationFinder(my_location_result);        

        initialize();        
    }
	
	public void initialize() {
		
		// Open DB
		//DataHelper openHelper = new DataHelper(this);
		//db = openHelper.getWritableDatabase();
		
		// Show the indeterminate progress bar
		ProgressBar dialog = (ProgressBar)findViewById(R.id.progressBar1);
		dialog.setVisibility(View.VISIBLE);
		
		// Hide the refresh icon since we are using it
		my_image_view = (ImageView)findViewById(R.id.refresh_icon);
		my_image_view.setVisibility(View.INVISIBLE);
		
		 //my_location_finder = new LocationFinder(my_location_result);
		 my_location_finder.getCurrentLocation(getBaseContext(), isEmulator);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		my_location_finder.removeLocationListeners();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initialize();
	}
	
	public void refreshBtn_click(View view){
		initialize();
	}
	
	public LocationResult my_location_result = new LocationResult(){
		
		@Override
		public void gotLocation(Location location) {
			
			boolean isNewLocationFromProvider = my_location_finder.isLocationFound(); 
			
			// Change menu icons
			if (isNewLocationFromProvider) {
				ProgressBar dialog = (ProgressBar)findViewById(R.id.progressBar1);
				dialog.setVisibility(View.INVISIBLE);
				
				my_image_view = (ImageView)findViewById(R.id.refresh_icon);
				my_image_view.setImageResource(R.drawable.refresh);
				my_image_view.setVisibility(View.VISIBLE);
			}
			
			try {
				
				int maxNumOfResults = Integer.parseInt(ParkingHelper.MAX_NUM_OF_RESULTS);
				
				final ArrayList<CarparkNow> array_search_results_short_list = ParkingHelper.getNearestParkingLots(getBaseContext(), location, maxNumOfResults);
			
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
		        
		        // Store the search results so that they can be accessed by the map activity
		        //storeSearchResults(array_search_results_short_list, maxNumOfResults);		
			}
				
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.parkingmenu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.settingsMenu:
	        showSettings();
	        return true;
	    case R.id.mapMenu:
	    	showMaps();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void showMaps() {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(this, MyMapActivity.class));
		startActivity(intent);
	}
	
	private void showSettings(){
		Intent settingsActivity = new Intent(getBaseContext(), PreferencesActivity.class);
		startActivity(settingsActivity);
	}
}
	
	/*
	private void storeSearchResults(ArrayList<CarparkNow> array_search_results, int maxNumOfResults) {
        
		// Delete all previous search results
		deleteAllRecords();
		
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
	
	private void deleteAllRecords(){
		this.db.delete(DataHelper.TABLE_NAME, null, null);
	}
	
}
	/*
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
	    	    this.dialog.dismiss();
	    	    
	            //if(this.dialog.isShowing())
	    	    if (!my_location_finder.isLocationFound())
	            {	
	            	AlertDialog alertDialog = new AlertDialog.Builder(Parking.this).create();
	                alertDialog.setTitle("Warning");
	                alertDialog.setMessage("Tough break, kid.  You didn't get a location.");
	                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                  public void onClick(DialogInterface dialog, int which) {
	                    return;
	                } });
	            }
	            
	            else
	            {
	            	
	            }
	        }
	}
	
	*/
