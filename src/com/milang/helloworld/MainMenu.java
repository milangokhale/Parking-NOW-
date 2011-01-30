package com.milang.helloworld;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;

public class MainMenu extends Activity {
	
		
	@Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.mainmenu);   
    }
	
	public void MapsBtn_Click(View view)
	{	
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(this, MapMain.class));
		startActivity(intent);
	}
	
	public void TwitterBtn_Click(View view)
	{
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(this, Twitter.class));
		startActivity(intent);		
	}
	
	public void ParkingBtn_Click(View view)
	{
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(this, Parking.class));
		startActivity(intent);		
	}
}