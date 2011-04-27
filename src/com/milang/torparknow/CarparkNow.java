/**
 * 
 */
package com.milang.torparknow;

/**
 * @author milang
 * CarParkNow is an extension of CarPark by Adam W that includes fields for distance from current location. 
 * 
 */
public class CarparkNow extends Carpark {

	private Double calcDistance;
	private static final long serialVersionUID = 1L;

    public Double getCalcDistance(){
    	return calcDistance;
    }
    
    public void setCalcDistance(Double calcDistance){
    	this.calcDistance = calcDistance;
    }
	
}
