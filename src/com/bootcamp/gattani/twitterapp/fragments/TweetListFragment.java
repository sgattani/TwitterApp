package com.bootcamp.gattani.twitterapp.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bootcamp.gattani.twitterapp.R;
import com.bootcamp.gattani.twitterapp.adapters.TweetsAdapter;
import com.bootcamp.gattani.twitterapp.listeners.EndlessScrollListener;
import com.bootcamp.gattani.twitterapp.models.Tweet;
import com.loopj.android.http.RequestParams;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetListFragment extends Fragment{
	
	protected PullToRefreshListView lvTweets;
	protected ArrayList<Tweet> tweets;
	protected TweetsAdapter tweetLvAdapter;
	protected String screenName;
	
	public enum GET {
		ON_LOAD,
		ON_SCROLL,
		ON_REFRESH
	};

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		tweetLvAdapter = new TweetsAdapter(getActivity(), tweets);
		//find screen name if possible
		Intent i = getActivity().getIntent();
		if(i != null && i.getExtras() != null){
	    	screenName = i.getStringExtra("screen_name");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		lvTweets = (PullToRefreshListView)v.findViewById(R.id.lvTweets);			
		lvTweets.setAdapter(tweetLvAdapter);
		
		//setup the endless scroll
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void loadMore(int page, int totalItemsCount) {  	  
				//artificially clipped to not load more than 200 tweets when scrolling 
				if(tweets != null && !tweets.isEmpty() && tweets.size() < 200){
					getTweetsByInvoction(GET.ON_SCROLL);
				} else {
					Log.d("DEBUG", "End of tweets ...");
				}    			
			}
		});
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getTweetsByInvoction(GET.ON_REFRESH);
			}
		});

		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//get timeline feed
		getTweetsByInvoction(GET.ON_LOAD);

	}
	
	/**
	 * Constructs the parameters to call the Twitter API with and UI treatment 
	 * based on user action.
	 * 
	 * @param invoked
	 */
	public void getTweetsByInvoction(GET invoked){
		RequestParams rparams = null;
		boolean overWriteLocal = false;;
		boolean resetPosition = true;
		
		switch(invoked){
		case ON_LOAD:
			//not much to do here. this would get the default number of tweets and store them to db
			rparams = null;
			overWriteLocal = false;
			resetPosition = false;
			break;
			
		case ON_SCROLL:
			//construct the request params to fetch only older tweets
			if(tweets != null && !tweets.isEmpty()){
				rparams = new RequestParams();
				rparams.put("max_id", String.valueOf(tweets.get(tweets.size() - 1).getTweetId()));
			} else {
				rparams = null;
			}
			//set overwrite to true
			overWriteLocal = true;
			resetPosition = false;
			break;
			
		case ON_REFRESH:
			//request only the tweets that are newer than the latest we have
			if(tweets != null && !tweets.isEmpty()){
				rparams = new RequestParams();
				rparams.put("since_id", String.valueOf(tweets.get(0).getTweetId()));
			} else {
				rparams = null;
			}
			
			//set overwrite to true
			overWriteLocal = true;
			resetPosition = true;
			break;
		}		
		
		getHomeTimeline(rparams, overWriteLocal, resetPosition);

	}

	/**
	 * Gets and persists the tweets based on request parameters. Applies
	 * UI treatment to scroll top and reset the refresh spinner
	 *  
	 * @param rparams
	 * @param overWriteLocal
	 * @param resetView
	 */
	abstract protected void getHomeTimeline(RequestParams rparams, final boolean overWriteLocal, final boolean resetView);
}
