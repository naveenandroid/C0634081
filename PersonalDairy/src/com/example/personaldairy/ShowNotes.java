package com.example.personaldairy;

import java.io.ByteArrayOutputStream;

import java.io.File;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personaldairy.dto.NoteItem;
import com.example.personaldairy.model.DairyNotes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowNotes extends Activity{


	private TextView notes;
	private TextView Date;
	private ImageView ivPic;
	private GPSTracker mGPSTracker;
	private GoogleMap googleMap;
	private Cursor mCursor;
	private DairyNotes mDairyNotes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_notes);
		mGPSTracker = new GPSTracker(this);
		mDairyNotes = new DairyNotes(this);
		notes = (TextView)findViewById(R.id.etDairyNote);
		Date = (TextView)findViewById(R.id.etdate);
		ivPic = (ImageView)findViewById(R.id.insertPic);

		Intent in = getIntent();
		int id = in.getIntExtra("id", 0);

		mCursor= mDairyNotes.getRecord(id);
		showSqlData(mCursor);


		try {
			initilizeMap();

			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(mGPSTracker.latitude, mGPSTracker.longitude));

			// Changing marker color
			marker.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

			googleMap.addMarker(marker);

			CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(new LatLng(mGPSTracker.latitude, mGPSTracker.longitude)).zoom(15).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initilizeMap() 
	{
		if (googleMap == null) 
		{
			// Creating a google map using MapFragment class
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) 
			{
				Toast.makeText(ShowNotes.this, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
	}


	private void showSqlData(Cursor cursor) {
		String message = null;
		String date = null;
		String image = null;
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(0);
				message = cursor.getString(1);
				date = cursor.getString(2);
				image = cursor.getString(3);

				System.out.println("message"+message);
			} while (cursor.moveToNext());
		}

		notes.setText(message);
		Date.setText(date);
		
		File imgFile = new  File(image);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        //Drawable d = new BitmapDrawable(getResources(), myBitmap);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
        ivPic.setImageBitmap(myBitmap);
        


	}
}
