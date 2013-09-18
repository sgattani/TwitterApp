package com.bootcamp.gattani.twitterapp.fragments;

import java.util.Arrays;
import java.util.Collections;

import org.json.JSONArray;

import android.util.Log;

import com.bootcamp.gattani.twitterapp.MyTwitterApp;
import com.bootcamp.gattani.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HomeTimelineFragment extends TweetListFragment {

	
	/**
	 * Gets and persists the tweets based on request parameters. Applies
	 * UI treatment to scroll top and reset the refresh spinner
	 *  
	 * @param rparams
	 * @param overWriteLocal
	 * @param resetView
	 */
	@Override
	protected void getHomeTimeline(RequestParams rparams, final boolean overWriteLocal, final boolean resetView){	
		//get timeline feed
		MyTwitterApp.getRestClient().getHomeTimeline(rparams, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets){
				tweets.addAll(Tweet.fromJson(jsonTweets));
				Collections.sort(tweets);
				Log.d("DEBUG", Arrays.deepToString(tweets.toArray()));
				tweetLvAdapter.notifyDataSetChanged();
				lvTweets.onRefreshComplete();
				if(resetView){
					//scroll to top
					lvTweets.smoothScrollToPosition(0);
				}
				
				if(overWriteLocal){
					Tweet.overWriteTweets(tweets);					
				} else {
					Tweet.storeTweets(tweets);
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content){
				Log.d("DEBUG", "Offline ... loading stored tweets");
				tweets.clear();
				tweets.addAll(Tweet.getStoredTweets());
				Collections.sort(tweets);
				lvTweets.smoothScrollToPosition(0);
				tweetLvAdapter.notifyDataSetChanged();
				lvTweets.onRefreshComplete();
				return;
			}
		});				
	}
}
