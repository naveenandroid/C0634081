package com.example.personaldairy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.personaldairy.model.DairyNotes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddNotes extends Activity implements OnClickListener{

	private GoogleMap googleMap;

	private EditText etDairyNote;
	private Button btnSave;
	private Button btnCancel;

	private ImageView insertPic;
	private GPSTracker mGPSTracker;

	private String selectedImagePath;

	private int SELECT_PICTURE=1;

	private String mDairyNote_string;

	private DairyNotes mDairyNotes;
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_notes);
		mDairyNotes = new DairyNotes(this);
		mGPSTracker = new GPSTracker(this);
		etDairyNote = (EditText)findViewById(R.id.etDairyNote);

		insertPic = (ImageView)findViewById(R.id.insertPic);
		btnSave = (Button)findViewById(R.id.btnSave);
		btnCancel = (Button)findViewById(R.id.btnCancel);

		insertPic.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
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
				Toast.makeText(AddNotes.this, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.insertPic:
			Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
			break;
		case R.id.btnSave:
			mDairyNote_string=etDairyNote.getText().toString();
			Calendar c = Calendar.getInstance(); 
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			String date = df.format(new Date());
			mDairyNotes.addSqlData(mDairyNote_string,date,selectedImagePath,mGPSTracker.latitude,mGPSTracker.longitude);
			finish();
			break;
		case R.id.btnCancel:
			finish();
			break;

		default:
			break;
		}

	}

	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == RESULT_OK) {
	            if (requestCode == SELECT_PICTURE) {
	                Uri selectedImageUri = data.getData();
	                selectedImagePath = getPath(selectedImageUri);
	                System.out.println("Image Path : " + selectedImagePath);
	                File imgFile = new  File(selectedImagePath);
	                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
	                //Drawable d = new BitmapDrawable(getResources(), myBitmap);
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                myBitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
	                insertPic.setImageBitmap(myBitmap);
	            }
	        }
	    }
	 
	 public String getPath(Uri uri) {
         // just some safety built in 
         if( uri == null ) {
             // TODO perform some logging or show user feedback
             return null;
         }
         // try to retrieve the image from the media store first
         // this will only work for images selected from gallery
         String[] projection = { MediaStore.Images.Media.DATA };
         Cursor cursor = managedQuery(uri, projection, null, null, null);
         if( cursor != null ){
             int column_index = cursor
             .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
             cursor.moveToFirst();
             return cursor.getString(column_index);
         }
         // this is our fallback here
         return uri.getPath();
 }
}
