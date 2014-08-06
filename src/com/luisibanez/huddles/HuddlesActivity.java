package com.luisibanez.huddles;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class HuddlesActivity extends ActionBarActivity{
	
	public static final String NEWS_ID_KEY = "news_id";
	public static final String FRIENDS_KEY = "friends_list";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_fragment_activity);
		
		HuddlesFragment huddlesFragment = HuddlesFragment.newInstance();
		huddlesFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, huddlesFragment).commit();
	}
}
