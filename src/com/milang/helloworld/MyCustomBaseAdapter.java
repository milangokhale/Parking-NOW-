/**
 * 
 */
package com.milang.helloworld;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.milang.location.LocationUtil;
import com.milang.util.*;

/**
 * @author milang
 *
 */
public class MyCustomBaseAdapter extends BaseAdapter {
	
	 private static ArrayList<Carpark> car_park_array_list;
	 
	 private LayoutInflater mInflater;

	 public MyCustomBaseAdapter(Context context, ArrayList<Carpark> results) {
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
	   holder.tvRate = (TextView) convertView.findViewById(R.id.carpark_rate);
	   holder.tvCapacity = (TextView) convertView.findViewById(R.id.carpark_capacity);
	   holder.tvStreetAddress = (TextView) convertView.findViewById(R.id.carpark_street_address);
	   //holder.txtCalculatedDistance = (TextView) convertView.findViewById(R.id.carpark_calculated_distance);
	   
	   convertView.setTag(holder);
	  } else {
	   holder = (ViewHolder) convertView.getTag();
	  }
	  
	  //String calc_distance_string 
	  
	  //Double calcDistance = LocationUtil.calcDistanceInKm(car_park_array_list.get(position).getLat(), 
		//	  car_park_array_list.get(position).getLng(), p2);
	  
	  //NumberFormat.roundNumber

	  //holder.txtCalculatedDistance.setText(calcDistance.toString() + " km");

	  holder.tvRate.setText(car_park_array_list.get(position).getRate());
	  holder.tvCapacity.setText(car_park_array_list.get(position).getCapacity());
	  holder.tvStreetAddress.setText(car_park_array_list.get(position).getStreetAddress());

	  return convertView;
	 }

	 static class ViewHolder {
		TextView txtAddress;
		TextView txtCalculatedDistance;
		TextView tvTitle;
		TextView tvFacilityType;
		TextView tvRate;
		TextView tvCapacity;
		TextView tvStreetAddress;
		TextView tvUrl;
	 }
	}