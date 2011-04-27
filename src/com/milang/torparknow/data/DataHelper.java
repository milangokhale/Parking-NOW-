/**
 * 
 */
package com.milang.torparknow.data;

import android.content.Context;
import android.database.sqlite.*;

/**
 * @author milang
 *
 */

public class DataHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "library.db";
	
	public static final String TABLE_NAME = "table1";	
	public static final String TITLE = "title";
	public static final String VALUE = "value";	
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DataHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, value REAL)");
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
       onCreate(db);
    } 
}
