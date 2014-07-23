package com.example.personaldairy.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DairyNotes extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "DATABASE_DAIRYNOTES";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE = "TABLE_DAIRYNOTES";
	public static final String NOTES = "NOTES";
	public static final String DATE = "DATE";
	public static final String PATH = "PATH";
	public static final String LAT = "LAT";
	public static final String LON = "LON";
	SQLiteDatabase db = this.getReadableDatabase();
	public DairyNotes(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase Database) {

		String sqlQuery = "create table " + TABLE + "( " + BaseColumns._ID+ " integer primary key autoincrement, " + NOTES + " text not null, " + DATE + " text not null," + PATH + " text not null," + LAT + " text not null," + LON + " text not null);";
		Database.execSQL(sqlQuery);
	}

	
	public boolean deleterow(int rowId) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(this.TABLE,  BaseColumns._ID +"=" +rowId, null) > 0;
	}
	
	public void addSqlData(String message,String date,String path,double lat,double lon) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NOTES, message);
		values.put(DATE, date);
		values.put(PATH, path);
		values.put(LAT, lat);
		values.put(LON, lon);
		db.insert(TABLE, null, values);
	}
	public Cursor getSqlData() {
		
		Cursor cursor = db.query(TABLE, null, null, null, null, null,null);
		return cursor;
	}
	
	public Cursor getSqlSearchData(String search)
	{
		Cursor searchCursor = db.rawQuery("SELECT * FROM " + TABLE + " where " +NOTES+ " like '%"+search+"%'" , null);
		return searchCursor;
	}
	
	public Cursor getRecord(int id)
	{
		Cursor searchCursor = db.rawQuery("SELECT * FROM " + TABLE + " where _ID ="+id+"" , null);
		return searchCursor;
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	public boolean UpdateStatus(String rowId,String stat) 
	{

		db = this.getWritableDatabase();

		ContentValues args1 = new ContentValues();
		args1.put(NOTES, stat);
		return db.update(TABLE,args1,  BaseColumns._ID +"=" +rowId,null) > 0;


	}



}
