package com.milang.helloworld;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Twitter extends Activity {
	
	TextView tvData;
		
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.twitter); 
       
       tvData = (TextView)findViewById(R.id.TextView12345);
       
       SearchResults sr1 = new SearchResults("25 Dundas", 122.0);
       SearchResults sr2 = new SearchResults("34 Queen St", 45.0);
       SearchResults sr3 = new SearchResults("82 Darnborough", 1111.0);
       
       ArrayList<SearchResults> array_sr = new ArrayList<SearchResults>();
       
       array_sr.add(sr1);
       array_sr.add(sr2);
       array_sr.add(sr3);
       
       Collections.sort(array_sr, new Comparator<Object>(){
    	   
           public int compare(Object o1, Object o2) {
        	   SearchResults p1 = (SearchResults) o1;
        	   SearchResults p2 = (SearchResults) o2;
        	   
        	   return p1.getCalcDistance().compareTo(p2.getCalcDistance());
              //return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
           }
       });
       
       String x = "";
       for (int i=0; i < array_sr.size(); i++) {
    	   x+=array_sr.get(i).getAddress() + " " + array_sr.get(i).getCalcDistance() + "km" + "\n";
       }
       
       tvData.setText(x);
    
       //examineJSONFile();
       
       
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
            //tvData.setText(x);
        }
        catch (Exception ex)
        {
            //tvData.setText("Error w/file: " + ex.getMessage());
        }
    }
}