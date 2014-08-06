package com.luisibanez.huddles;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class RequestHuddleActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_fragment_activity);
		
		RequestHuddleFragment requestHuddlesFragment = RequestHuddleFragment.newInstance();
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, requestHuddlesFragment).commit();
	}
}
