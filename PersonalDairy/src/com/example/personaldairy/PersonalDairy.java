package com.example.personaldairy;

import android.R;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PersonalDairy extends Activity {

	Handler mSplashHandler;
	Runnable action;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		mSplashHandler = new Handler();
		action = new Runnable()
		{
			@Override
			public void run()
			{
				Intent intent= new Intent(PersonalDairy.this, Dairy.class);
				startActivity(intent);
				finish();
			}
		};

		mSplashHandler.postDelayed(action, 2000);


	}


}
