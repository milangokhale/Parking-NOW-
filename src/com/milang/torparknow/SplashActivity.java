package com.milang.torparknow;


import com.milang.location.LocationFinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;

public class SplashActivity extends Activity {
	
	protected boolean _active = true;
    protected int _splashTime = 3000; // show splash for 3 secs
    
    
    public LocationFinder my_location_finder;
	
		
	@Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.mainmenu);   
	       
	     //my_location_finder = new LocationFinder(my_location_result);
		 //my_location_finder.getCurrentLocation(getBaseContext(), false);
	       
	    // thread for displaying the SplashScreen
	        Thread splashThread = new Thread() {
	            @Override
	            public void run() {
	                try {
	                    int waited = 0;
	                    while(_active && (waited < _splashTime)) {
	                        sleep(100);
	                        if(_active) {
	                            waited += 100;
	                        }
	                    }
	                } catch(InterruptedException e) {
	                    // do nothing
	                } finally {
	                    finish();
	                    
	                    ParkingHelper.initSettings(getBaseContext());
	            		
	            		Intent intent = new Intent();
	            			            		
	            		if (ParkingHelper.DISPLAY_TYPE.equalsIgnoreCase("Map")) {
	            			intent.setComponent(new ComponentName(getBaseContext(), MyMapActivity.class));
	            		}
	            		
	            		else {
	            			intent.setComponent(new ComponentName(getBaseContext(), ParkingActivity.class));
	            		}
	            		
	            		startActivity(intent);	
	                    stop();
	                }
	            }
	        };
	        splashThread.start();
	        
	        //new ShowProgressDialog().execute();
    }
	
	//public LocationResult my_location_result = new LocationResult(){
		
		public void MapsBtn_Click(View view)
	{	
		//Intent intent = new Intent();
		//intent.setComponent(new ComponentName(this, MapMain.class));
		//startActivity(intent);
	}
	
	public void ParkingBtn_Click(View view)
	{
		//Intent intent = new Intent();
		//intent.setComponent(new ComponentName(this, Parking.class));
		//startActivity(intent);		
	}
}