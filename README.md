TwitterApp
==========

A slightly more mature twitter client for Android

# User Stories:

* User can sign in using OAuth login flow
* User can view last 25 tweets from their home timeline
* User should be able to see the user, body and timestamp for tweet
* User can compose a new tweet
* User can click a “Compose” icon in the Action Bar on the top right
* User will have a Compose view opened
* User can enter a message and hit a button to Post
* User should be taken back to home timeline with new tweet visible
* User can load more tweets once they reach the bottom
  - Endless scroll
* User can open the twitter app offline and see recent tweets
  - Tweets are persisted into sqlite and displayed from the local DB
  - Only hometimeline has offline access. Mentions and UserTimeline does not
* User can pull down the list to refresh.
  - Uses <https://github.com/erikwt/PullToRefresh-ListView> 
* User can switch between Timeline and Mention views using tabs.
  - User can view their home timeline tweets.
  - User can view the recent mentions of their username.
  - User has endless scroll to bottom of the list and new tweets will load
* User can click icon on Action Bar to view their profile
  - User can see picture, tagline, # of followers, # of following, and tweets on their profile.
* User can click on the profile image in a tweet to see that user's profile. 
  
