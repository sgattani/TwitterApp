package com.bootcamp.gattani.twitterapp.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.bootcamp.gattani.twitterapp.MyTwitterApp;
import com.bootcamp.gattani.twitterapp.R;
import com.bootcamp.gattani.twitterapp.fragments.HomeTimelineFragment;
import com.bootcamp.gattani.twitterapp.fragments.MentionsTimelineFragment;

public class HomeTimelineActivity extends FragmentActivity implements TabListener{	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_timeline);
		setupNavigationTabs();
		
	}
	
	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tabHome = actionBar.newTab()
								.setText(getText(R.string.home_tab_text))
								.setTag("HomeTimelineFragment")
								.setIcon(R.drawable.ic_home_n)
								.setTabListener(this);

		Tab tabMentions = actionBar.newTab()
				.setText(getText(R.string.mentions_tab_text))
				.setTag("MentionsTimelineFragment")
				.setIcon(R.drawable.ic_mentions)
				.setTabListener(this);
		
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds the compose and refresh
		getMenuInflater().inflate(R.menu.home_timeline, menu);
		return true;
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_compose:
			Intent i = new Intent(getBaseContext(), ComposeTweetActivity.class);
			startActivityForResult(i, ComposeTweetActivity.COMPOSE_TWEET_ACTIVITY_ID);
			break;
			
		case R.id.action_logout:
			//logout and send to logged out activity
			MyTwitterApp.getRestClient().clearAccessToken();
			Intent logout = new Intent(getBaseContext(), LoggedOutActivity.class);
			logout.putExtra("action", "logout");
			startActivity(logout); 
			break;
			
		case R.id.menu_profile:
			Intent profile = new Intent(getBaseContext(), ProfileActivity.class);
			startActivity(profile); 
			break;
			
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ComposeTweetActivity.COMPOSE_TWEET_ACTIVITY_ID) {
			if (resultCode == Activity.RESULT_OK) {
				//getHomeTimeLineByInvoction(GET.ON_REFRESH);
			}			
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if(tab.getTag().equals("HomeTimelineFragment")) {
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
		} else {
			fts.replace(R.id.frame_container, new MentionsTimelineFragment());
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

}
