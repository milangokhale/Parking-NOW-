/**
 * 
 */
package com.milang.util;

import java.text.DecimalFormat;

/**
 * @author milang
 *
 */
public class NumberFormat {
	
	/**
     * Rounds an input double number to a specified number of decimal places.
     * 
     * @param num Number that is to be shortened to two decimal places.
     * @param numOfDecimals The number of decimals that the number is to be shortened to.
     * @return A double number to two decimal places.
     */	
	public static double roundNumber(double num, int numOfDecimals) 
	{		
		String str_num_of_decimals = "";
		DecimalFormat my_decimal_format;
		
		if (numOfDecimals < 1)
			my_decimal_format = new DecimalFormat("#");
		
		else {
			for (int i=0; i < numOfDecimals; i++ ) {
				str_num_of_decimals += "#";
			}
			
			my_decimal_format = new DecimalFormat("#." + str_num_of_decimals.toString());
		}
		
		return Double.valueOf(my_decimal_format.format(num));
	}
}
