package com.milang.helloworld;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Twitter extends ListActivity {
	
	TextView tvData;
		
	@Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.list);     
	       
	       tvData = (TextView)findViewById(R.id.TextView12345);
	       
	       examineJSONFile();
    }
	
	 // Takes a JSON class
	protected void examineJSONFile()
    {
        try
        {
            String x = "";
            
            // InputStream is a base class for reading data at a byte-level
            // In this case we open a text file from the raw/milang_json.txt file
            InputStream is = this.getResources().openRawResource(R.raw.milang_json);
            
            // Create byte array that reads the input stream
            byte [] buffer = new byte[is.available()];
            
            // While there is still a byte in the InputStream object, create a new string object and put it
            // into a JSON array object
            while (is.read(buffer) != -1);
            String jsontext = new String(buffer);
            JSONArray entries = new JSONArray(jsontext);

            // Write out the number of entries in the JSONArray object
            x = "JSON parsed.\nThere are [" + entries.length() + "]\n\n";

            // For all entries, add to the string the following JSON object information
            int i;
            for (i=0;i<entries.length();i++)
            {
                JSONObject post = entries.getJSONObject(i);
                x += "------------\n";
                x += "Date:" + post.getString("created_at") + "\n";
                x += "Post:" + post.getString("text") + "\n\n";
            }
            
            // set TextView element with string
            tvData.setText(x);
        }
        catch (Exception je)
        {
            tvData.setText("Error w/file: " + je.getMessage());
        }
    }
}