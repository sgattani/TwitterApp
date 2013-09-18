package com.bootcamp.gattani.twitterapp.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bootcamp.gattani.twitterapp.MyTwitterApp;
import com.bootcamp.gattani.twitterapp.R;
import com.bootcamp.gattani.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// add navigating up
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    RequestParams rparams = null;
	    String screenName = null;
	    //get screen name from intent
	    Intent i = getIntent();
	    if(i != null && i.getExtras() != null){
	    	screenName = i.getStringExtra("screen_name");
		    rparams = new RequestParams();
			rparams.put("screen_name", screenName);	    	
	    }
	    
	    if(screenName != null){
			MyTwitterApp.getRestClient().getUser(rparams, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject userInfo){
					user = User.fromJson(userInfo);
					if(user != null){
						//set action bar
						getActionBar().setTitle("@".concat(user.getScreenName()));
						populateProfileHeader(user);
					}
				}
			});
	    } else {
			//get current user
			MyTwitterApp.getRestClient().getCurrentUser(null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject userInfo){
					user = User.fromJson(userInfo);
					if(user != null){
						//set action bar
						getActionBar().setTitle("@".concat(user.getScreenName()));
						populateProfileHeader(user);
					}
				}
			});	    	
	    }
	}
	
	private void populateProfileHeader(User u) {
		TextView tvName = (TextView)findViewById(R.id.tvName);
		TextView tvTagline = (TextView)findViewById(R.id.tvTagLine) ;
		TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
		
		tvName.setText(u.getName());
		tvTagline.setText(u.getTagline());
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFriendsCount() + " Friends");
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
	}

}
