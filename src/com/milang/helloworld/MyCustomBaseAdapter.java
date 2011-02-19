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

import com.milang.util.*;

/**
 * @author milang
 *
 */
public class MyCustomBaseAdapter extends BaseAdapter {
	
	 private static ArrayList<SearchResults> searchArrayList;
	 
	 private LayoutInflater mInflater;

	 public MyCustomBaseAdapter(Context context, ArrayList<SearchResults> results) {
	  searchArrayList = results;
	  mInflater = LayoutInflater.from(context);
	 }

	 public int getCount() {
	  return searchArrayList.size();
	 }

	 public Object getItem(int position) {
	  return searchArrayList.get(position);
	 }

	 public long getItemId(int position) {
	  return position;
	 }

	 public View getView(int position, View convertView, ViewGroup parent) {
	  ViewHolder holder;
	  if (convertView == null) {
	   convertView = mInflater.inflate(R.layout.custom_row_view, null);
	   holder = new ViewHolder();
	   holder.txtAddress = (TextView) convertView.findViewById(R.id.name);
	   holder.txtDistance = (TextView) convertView.findViewById(R.id.cityState);
	   //holder.txtAvailability = (TextView) convertView.findViewById(R.id.phone);

	   convertView.setTag(holder);
	  } else {
	   holder = (ViewHolder) convertView.getTag();
	  }
	  
	  //String calc_distance_string 
	  
	  Double calcDistance = NumberFormat.roundNumber(searchArrayList.get(position).getCalcDistance(), 2);
	  
	  holder.txtAddress.setText(searchArrayList.get(position).getAddress());
	  holder.txtDistance.setText(calcDistance.toString() + " km");

	  return convertView;
	 }

	 static class ViewHolder {
	  TextView txtAddress;
	  TextView txtDistance;
	  //TextView txtAvailability;
	  
	 }
	}