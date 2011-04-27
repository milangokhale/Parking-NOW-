/**
 * 
 */
package com.milang.torparknow;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.milang.util.*;

/**
 * @author milang
 *
 */
public class MyCustomBaseAdapter extends BaseAdapter {
	
	 private static ArrayList<CarparkNow> car_park_array_list;
	 private final static int THOUSAND = 1000;
	 
	 private LayoutInflater mInflater;

	 public MyCustomBaseAdapter(Context context, ArrayList<CarparkNow> results) {
	  car_park_array_list = results;
	  mInflater = LayoutInflater.from(context);
	 }

	 public int getCount() {
	  return car_park_array_list.size();
	 }

	 public Object getItem(int position) {
	  return car_park_array_list.get(position);
	 }

	 public long getItemId(int position) {
	  return position;
	 }

	 public View getView(int position, View convertView, ViewGroup parent) {
	  ViewHolder holder;
	  if (convertView == null) {
	   convertView = mInflater.inflate(R.layout.custom_row_view, null);
	   holder = new ViewHolder();
	   holder.tvCalcDistance = (TextView) convertView.findViewById(R.id.carpark_calcDistance);
	   holder.tvCapacity = (TextView) convertView.findViewById(R.id.carpark_capacity);
	   holder.tvStreetAddress = (TextView) convertView.findViewById(R.id.carpark_street_address);
	   holder.tvRate = (TextView) convertView.findViewById(R.id.carpark_rate);
	   holder.tvMetricType = (TextView) convertView.findViewById(R.id.metric_type);
	   
	   convertView.setTag(holder);
	  } else {
	   holder = (ViewHolder) convertView.getTag();
	  }
	  
	  
	  
	  Double xy = car_park_array_list.get(position).getCalcDistance();
	  

	  if (xy < 1) {
		  holder.tvMetricType.setText("METERS");
	  }
	  
	  else {
		  holder.tvMetricType.setText("KM");
	  }
	  
	  String roundedNumString = getNumString(xy);

	  //Float inputNum = getDistanceFromCurrentLocation(num);
	  
	  holder.tvCalcDistance.setText(roundedNumString);
	  holder.tvCapacity.setText(car_park_array_list.get(position).getCapacity());
	  holder.tvStreetAddress.setText(car_park_array_list.get(position).getStreetAddress());
	  holder.tvRate.setText(car_park_array_list.get(position).getRate());

	  return convertView;
	 }
	 
	 
	 private String getNumString(Double inputNum){
		 
		 // Show in meters instead of kms
		 if (inputNum < 1) {
			
			inputNum = inputNum * THOUSAND;
			Integer x = NumberFormat.roundNumber(inputNum);
			return x.toString();
			//roundedCalcDistance = num.toString() + " m";
		 }
		 
		 else {
			  inputNum = NumberFormat.roundNumber(inputNum,1);
			  return inputNum.toString();
		 }

		 
		 
		 /*
			
		 
		 String str_num_of_decimals = "";
			DecimalFormat my_decimal_format;
			
			Double n = new Double(num);
			
			// 
			if (numOfDecimals < 1) {
				Math.round(num);
				BigDecimal b = new BigDecimal(n.toString());
				b = b.setScale(0, RoundingMode.HALF_UP);
				
				int x = b.intValue();
				return x;
			}
			
			else {
				for (int i=0; i < numOfDecimals; i++ ) {
					str_num_of_decimals += "#";
				}
				
				my_decimal_format = new DecimalFormat("#." + str_num_of_decimals.toString());
				return Float.valueOf(my_decimal_format.format(num));
			}
		 

		float num = NumberFormat.roundNumber(inputNum, 2);
		
		*/
	 }	 

	 static class ViewHolder {
		TextView tvCalcDistance;
		TextView tvCapacity;
		TextView tvStreetAddress;
		TextView tvRate;
		TextView tvMetricType;
	 }
	}