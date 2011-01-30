package com.milang.helloworld;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class GPSLocationListener implements LocationListener 
{
  MapController mapController;
  MapView mapView;
  
  public GPSLocationListener(MapController mc, MapView mv) 
  {
	  mc = this.mapController;
	  mv = this.mapView;
  }

  public void onLocationChanged(Location location) {
    if (location != null) {
      GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
      
      Toast.makeText(this.mapView.getContext(),
          "Latitude: " + location.getLatitude() + 
          " Longitude: " + location.getLongitude(), 
          Toast.LENGTH_SHORT).show();
      
      mapController.animateTo(point);
      mapController.setZoom(16);
      mapView.invalidate();
    }
  }

/* (non-Javadoc)
 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
 */
public void onProviderDisabled(String arg0) {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
 */
public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
 */
public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	
}
}