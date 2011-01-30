package com.milang.helloworld;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MapMain extends MapActivity {
    
    LinearLayout linearLayout;
    MapView mapView;    
    List<Overlay> mapOverlays;
    Drawable drawable;
    MapHelloItemizedOverlay itemizedOverlay;    
    MapController mapController;
    GeoPoint point;
    MyLocationOverlay me;    
    
    LocationListener locationListener;
    LocationManager locationManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // All overlay elements on a map are held by the MapView object 
        mapView = (MapView) findViewById(R.id.mapview);
        
        // Add MyLocation onto the map
        me = new MyLocationOverlay(this, mapView);
        //me.getMyLocation();
        //mapView.getOverlays().add(me);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  

        /*
        try 
        {        
	        LocationManager l =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        l.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, locationListener);
	        
	        List<String> li = l.getAllProviders();
	        for (Iterator<String> iterator = li.iterator(); iterator.hasNext();) {
	                String string =  iterator.next();
	                Log.d("gps", string);
	        }
	        
	        if (l.getLastKnownLocation("gps")==null)
	        	
	            Log.d("gps", "null2"); 
    	}
        
        catch (Exception ex) 
        {
        	System.out.print("Error: " + ex.getMessage());
        }
        
        */
        
        // Allow user to zoom in/out with zoom slider
        mapView.setBuiltInZoomControls(true);     
        
        // Retrieve the List with getOverlays() methods
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.marker_red);
        itemizedOverlay = new MapHelloItemizedOverlay(drawable);
        
        // Coordinates for Ritika's place
        Double lat = 43.65419*1E6;
        Double lng = -79.37595*1E6;  
        
        // Create a new GeoPoint object and add it to the OverlayItem object
        point = new GeoPoint(lat.intValue(),lng.intValue());        
        
        // Get the MapController object of the current MapView object
        mapController = mapView.getController();
        
        // Center the map on the GeoPoint object instance 
        mapController.setCenter(point);
        
        // Set a zoom level, from 1-21.
        mapController.setZoom(17);
        
     // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
            	if (location != null) {
            	      GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
            	                  	      
            	      Toast.makeText(getBaseContext(),
            	          "Latitude: " + location.getLatitude() + 
            	          " Longitude: " + location.getLongitude(), 
            	          Toast.LENGTH_SHORT).show();
            	}
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
          };

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                60000, 
                200, 
                locationListener);

        // Add the OverlayItem object to the ItemizedOverlay object
        OverlayItem overlayitem = new OverlayItem(point, "", "");
        
        itemizedOverlay.addOverlay(overlayitem);        
        itemizedOverlay.addOverlay(new OverlayItem(getPoint(43.65311, -79.37702),"", ""));
        itemizedOverlay.addOverlay(new OverlayItem(getPoint(43.65647, -79.37951),"", ""));
        itemizedOverlay.addOverlay(new OverlayItem(getPoint(43.65299, -79.37423),"", ""));
        
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
    
    
    
    private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0),(int)(lon*1000000.0)));
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		me.disableCompass();
	}
	
	public void onResume() {
		super.onResume();
		
		me.enableCompass();
	}
	
	
}
