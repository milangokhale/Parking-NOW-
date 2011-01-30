package com.milang.helloworld;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;

public class Logon extends Activity {
		
	@Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.logon);
    }
	
	public void SignInBtn_Click(View view)
	{
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(this, MainMenu.class));
		startActivity(intent);		
	}
}