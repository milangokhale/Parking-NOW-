package com.milang.torparknow;

//import java.io.IOException;
import java.util.List;
//import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.milang.location.LocationUtil;
import com.milang.torparknow.data.DataHelper;

//import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
//import android.widget.Toast;

public class MyMapActivity extends MapActivity {
    
    LinearLayout linearLayout;
    MapView mapView;    
    List<Overlay> mapOverlays;
    Drawable drawable;
    MapItemizedOverlay itemizedOverlay;    
    MapController mapController;
    GeoPoint point;
    MyLocationOverlay me;    
    
    LocationListener locationListener;
    LocationManager locationManager;
    
	private SQLiteDatabase db;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
        
        Cursor c = fetchAllRecords();
        
        float xyz;
        float lngtest;
        
        int count = 0;
        
        c.moveToFirst();
        while (c.isAfterLast() == false) {
        	xyz = c.getFloat(0);
            lngtest = c.getFloat(1);
            GeoPoint p = LocationUtil.getGeoPointFromFloats(xyz, lngtest);
            itemizedOverlay.addOverlay(new OverlayItem(p,"", ""));
            count++;
            
            if (count ==1)
            	// Center the map on the GeoPoint object instance 
                mapController.setCenter(p);
            	
       	    c.moveToNext();
        }
        
        db.close();
        
        // Set a zoom level, from 1-21.
        mapController.setZoom(16);
        
        // Add the ItemizedOverlay object to the array of Overlay objects
        mapOverlays.add(itemizedOverlay); 
        
        /* This code would allow you to look up an address and geo-code its coordinates.
         * Looks like there is a bug in Android 2.2 that prevents the Geocoder from 
         * looking up the location correctly.
         * See: http://code.google.com/p/android/issues/detail?id=8816
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());    
        try {
            List<Address> addresses = geoCoder.getFromLocationName(
                "1001 Bay Street, Toronto, Ontario, Canada", 5);
            if (addresses.size() > 0) {
                point = new GeoPoint(
                        (int) (addresses.get(0).getLatitude() * 1E6), 
                        (int) (addresses.get(0).getLongitude() * 1E6));
                        
                mapController.animateTo(point);    
                mapView.invalidate();
            }    
            
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        *///	
    }
    
    public Cursor fetchAllRecords() {
    	
    	DataHelper openHelper = new DataHelper(getBaseContext());
    	db = openHelper.getReadableDatabase();
    	
    	return db.query(DataHelper.TABLE_NAME, new String[] {DataHelper.TITLE, DataHelper.VALUE}, null, null, null, null, null);
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
	    default:
	        return super.onOptionsItemSelected(item);
	    }
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
