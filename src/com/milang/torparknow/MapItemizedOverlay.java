package com.milang.torparknow;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;


/* Wrapper class around ItemizedOverlay, which is a collection of OverlayItem objects.
 */

public class MapItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();	
	
	// Default constructor
	public MapItemizedOverlay(Drawable defaultMarker) {
		
		// boundCenterBottom is generally associated with a pin 
		super(boundCenterBottom(defaultMarker));
	}

	// Create an OverlayItem object 
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}
	
	// Add the OverlayItem object to the Hello
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}

	// Return the number of overlays
	@Override
	public int size() {
		return mOverlays.size();
	}
	
	// add these methods when a user clicks on a location
	@Override
	protected boolean onTap(int i) {	    
		return true;
	}
}



