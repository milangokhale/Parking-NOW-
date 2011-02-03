package com.milang.helloworld;

import java.util.Calendar;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ProgressBarMain extends Activity {

	private ProgressBar mProgress;
	
	LocationListener locationListener;	
	LocationManager locationManager;
	
	boolean hasLocation = false; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progressbar);	
		
		mProgress = (ProgressBar)findViewById(R.id.progressbar_default);
		
   	 	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
   	 
   	 	locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

              // Called when a new location is found by the network location provider.
            	if (location != null) {
            	      GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
            	                  	      
            	      Toast.makeText(getBaseContext(),
            	          "Latitude: " + location.getLatitude() + 
            	          " Longitude: " + location.getLongitude(), 
            	          Toast.LENGTH_SHORT).show();
            	      
            	      hasLocation = true;
            	      
            	}
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
            
          };

		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 1000, locationListener);
		
		new GetLocationTask().execute();
	}
	
	
	public void onStop(){
    	super.onStop();
    	locationManager.removeUpdates(locationListener);
    }
	
	class GetLocationTask extends AsyncTask<Void, Integer, Void> {
		
		private final ProgressDialog dialog = new ProgressDialog(ProgressBarMain.this);

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

	
	
	/*
	
	

	boolean isRunning = false;
    
    Handler mHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg){
    		mProgress.incrementProgressBy(5);
    	}
    };
    
	
    public void onStart(){
    	super.onStart();
    	mProgress.setProgress(0);
    	
    	Thread background = new Thread(new Runnable(){
    		public void run(){
    			try {
    				for (int i=0; i<20 && isRunning; i++) {
    					Thread.sleep(1000);
    					mHandler.sendMessage(mHandler.obtainMessage());
    				}	
    			}
    			
    			catch (Throwable t) {
    				// end background thread
    			}
    		}
    	});
    	
    	isRunning = true;
    	background.start();
    }
	
    public void onStop(){
    	super.onStop();
    	isRunning = false;
    }
    
 
}
   */
