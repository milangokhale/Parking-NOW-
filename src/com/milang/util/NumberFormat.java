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
     * Rounds an input double number to two decimal places.
     * 
     * @param num Number that is to be shortened to two decimal places.
     * @return A double number to two decimal places.
     */	
	public static double roundNumTwoDecimals(double num) 
	{		
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(num));
	}
}
