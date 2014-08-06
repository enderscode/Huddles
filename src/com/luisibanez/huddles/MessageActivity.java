package com.luisibanez.huddles;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MessageActivity extends ActionBarActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_fragment_activity);
		
		MessageFragment messageFragment = MessageFragment.newInstance();
		messageFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, messageFragment).commit();
	}
}
