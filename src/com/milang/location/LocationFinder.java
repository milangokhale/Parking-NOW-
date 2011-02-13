/**
 * 
 */
package com.milang.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * @author milang
 * LocationFinder is a class that defines two types of location listener handlers (GPS and Network)
 * and returns current location.
 *
 */
public class LocationFinder {
	
    /**
	 * @return the isLocationFound
	 */
	public boolean isLocationFound() {
		return isLocationFound;
	}

	/**
	 * @param isLocationFound the isLocationFound to set
	 */
	public void setLocationFound(boolean isLocationFound) {
		this.isLocationFound = isLocationFound;
	}

	private final static long minTime = 60000;
    private final static float minDistance = 1000f;
    
    private LocationManager locationManager;    
    private LocationResult locationResult;
    
    /**
	 * @return the locationResult
	 */
	public LocationResult getLocationResult() {
		return locationResult;
	}

	/**
	 * @param locationResult the locationResult to set
	 */
	public void setLocationResult(LocationResult locationResult) {
		this.locationResult = locationResult;
	}

	boolean isGpsEnabled = false;
    boolean isNetworkEnabled = false;    
    boolean isLocationFound = false;
	
	/**
	 * Implements LocationListener for GPS
	 */
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location)
        {
        	isLocationFound = true;
        	locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerGps);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extra) {}
    };

    /**
	 * Implements LocationListener for Network Provider
	 */
    
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location)
        {
        	isLocationFound = true;
        	locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerNetwork);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extra) {}
    };
    
	/**
	 * Gets the current location.
	 * 
	 * @param context The context for this location search. 
	 * @param result?
	 * @return True if location found; false otherwise.
	 */
	public boolean getCurrentLocation(Context context, LocationResult result)
    {       
        locationResult = result;

        if(locationManager == null) { locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE); }
        
        //exceptions thrown if provider not enabled
        try { isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); }
        catch (Exception ex) {}
        
        try { isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); }
        catch (Exception ex) {}

        // If no providers are enabled, no location can be found
        if (!isGpsEnabled && !isNetworkEnabled){ return false; }
        
        if (isGpsEnabled) {locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
        		minTime, minDistance, locationListenerGps);}
        
        if (isNetworkEnabled) {locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
        		minTime, minDistance, locationListenerNetwork);}

        GetLastLocation();
        return true;
    }
	
    private void GetLastLocation()
    {
        locationManager.removeUpdates(locationListenerGps);
        locationManager.removeUpdates(locationListenerNetwork);

        Location gpsLocation = null;
        Location networkLocation = null;

        if (isGpsEnabled) { gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); }
        
        if (isNetworkEnabled) { networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);}

        // If there are both values use the latest one
        if(gpsLocation != null && networkLocation != null)
        {
            if(gpsLocation.getTime() > networkLocation.getTime())
            {
                locationResult.gotLocation(gpsLocation);
            }
            else
            {
                locationResult.gotLocation(networkLocation);
            }

            return;
        }
        
        // If only GPS location found
        if(gpsLocation != null)
        {
            locationResult.gotLocation(gpsLocation);
            return;
        }

        // If only network location found
        if(networkLocation != null)
        {
            locationResult.gotLocation(networkLocation);
            return;
        }

        locationResult.gotLocation(null);
    }

    public static abstract class LocationResult
    {
        public abstract void gotLocation(Location location);
    }
}