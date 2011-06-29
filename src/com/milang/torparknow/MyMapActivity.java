package com.milang.torparknow;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.milang.location.LocationFinder;
import com.milang.location.LocationFinder.LocationResult;
import com.milang.location.LocationUtil;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MyMapActivity extends MapActivity {
    
    LinearLayout linearLayout;
    MapView mapView;    
    List<Overlay> mapOverlays;
    Drawable drawable;
    MapItemizedOverlay itemizedOverlay;
    MapController mapController;
    GeoPoint point;
	
	ImageView my_image_view;
	
	LocationFinder my_location_finder;
	
	MyLocationOverlay myLocationOverlay;

	private final boolean isEmulator = "google_sdk".equals(android.os.Build.PRODUCT);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Show the indeterminate progress bar
		ProgressBar dialog = (ProgressBar)findViewById(R.id.progressBar1);
		dialog.setVisibility(View.VISIBLE);
		
		// Hide the refresh icon since we are using it
		my_image_view = (ImageView)findViewById(R.id.refresh_icon);
		my_image_view.setVisibility(View.INVISIBLE);

        initMap();
        
        initLocation();        
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
		initLocation();
	}
    
    private void initLocation(){

		 my_location_finder = new LocationFinder(my_location_result);
		 my_location_finder.getCurrentLocation(getBaseContext(), isEmulator);
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
			
			int maxNumOfResults = 4;
			final ArrayList<CarparkNow> array_search_results = ParkingHelper.getNearestParkingLots(getBaseContext(), location, maxNumOfResults);
			
			int count = 0;
			for (int i=0; i < maxNumOfResults; i++) {
	        	float lat = array_search_results.get(i).getLat();
	        	float lng = array_search_results.get(i).getLng();
	        	
	        	GeoPoint p = LocationUtil.getGeoPointFromFloats(lat, lng);
	        	
	        	itemizedOverlay.addOverlay(new OverlayItem(p,"", ""));
	        	count++;
	        	
	        	if (!isEmulator){
		        	if (count ==1) {
		            	// Center the map on the GeoPoint object instance 
		                //mapController.setCenter(p);
		        		
		        		//TODO: This fails on the emulator, but works on the device -
		        		// because location is null on the emulator (hard-coded)
		        		GeoPoint current_location = LocationUtil.getGeoPointFromLocation(location);
		        		itemizedOverlay.addOverlay(new OverlayItem(current_location,"", ""));
		        		mapController.setCenter(current_location);
		        	}
	        	}
	        }
			
	        // Add the ItemizedOverlay object to the array of Overlay objects
	        mapOverlays.add(itemizedOverlay);
		}
	};
	
	private void initMap(){
		
		// All overlay elements on a map are held by the MapView object 
        mapView = (MapView) findViewById(R.id.mapview);
        
        // Allow user to zoom in/out with zoom slider
        mapView.setBuiltInZoomControls(true);     
        
        // Retrieve the List with getOverlays() methods
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.marker_red);
        itemizedOverlay = new MapItemizedOverlay(drawable);
        
        // Get the MapController object of the current MapView object
        mapController = mapView.getController();
        
        // Set a zoom level, from 1-21.
        mapController.setZoom(16);
        
        myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapOverlays.add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();
	}
    
    public void initialize(){
        
        //JSONObject j = getLocationInfo("1000 Bay St Toronto");
        //GeoPoint g = getGeoPoint(j);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mapmenu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.settings:
	        showSettings();
	        return true;
	    case R.id.list:
	    	showList();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void showSearchable() {
		//Intent intent = new Intent();
		//intent.setComponent(new ComponentName(this, SearchableActivity.class));
		//startActivity(intent);
		
		Toast.makeText(getBaseContext(), "featuer to come", Toast.LENGTH_LONG);
	}
	
	public void showList() {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(this, ParkingActivity.class));
		startActivity(intent);
	}
	
	private void showSettings(){
		Intent settingsActivity = new Intent(getBaseContext(), PreferencesActivity.class);
		startActivity(settingsActivity);
	}
}
