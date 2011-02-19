/**
 * 
 */
package com.milang.helloworld;

import java.util.Comparator;

/**
 * @author milang
 *
 */
public class SearchResults implements Comparator<Double> {
	 private String address = "";
	 private String availability = "";
	 private String distance = "";
	 private Double calcDistance;
	 
	 public SearchResults(String address, Double calcDistance) {
		 this.address = address;
		 //this.availability = availability;
		 //this.distance = distance;
		 this.calcDistance = calcDistance;
	 }
	 
	 public SearchResults(){
		 
	 }
	 
	// Comparator interface requires defining compare method.
	 public int compare(Double calculatedDistance, Double longestDistance) {
		 return calculatedDistance.compareTo(longestDistance);
	 }

	 public void setAddress(String address) {
	  this.address = address;
	 }

	 public String getAddress() {
	  return address;
	 }

	 public void setAvailability(String availability) {
	  this.availability = availability;
	 }

	 public String getAvailability() {
	  return availability;
	 }

	 public void setDistance(String distance) {
	  this.distance = distance;
	 }

	 public String getDistance() {
	  return distance;
	 }
	 
	 public void setCalcDistance(Double calcDistance) {
		  this.calcDistance = calcDistance;
		 }
	 
	 public Double getCalcDistance(){
		 return calcDistance;
	 }
}