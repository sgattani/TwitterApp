package com.bootcamp.gattani.twitterapp.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bootcamp.gattani.twitterapp.MyTwitterApp;
import com.bootcamp.gattani.twitterapp.R;
import com.bootcamp.gattani.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	private User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// add navigating up
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
		//get current user
		MyTwitterApp.getRestClient().getCurrentUser(null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userInfo){
				currentUser = User.fromJson(userInfo);
				if(currentUser != null){
					//set action bar
					getActionBar().setTitle("@".concat(currentUser.getScreenName()));
					populateProfileHeader(currentUser);
				}
			}
		});
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
