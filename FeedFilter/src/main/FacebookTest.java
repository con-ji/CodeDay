package main;

import java.util.List;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;

public class FacebookTest {
	
	private static final String[] KEYWORDS = {"shared", "likes", "commented", "replied","tagged"};
	private static final String USERTOKEN = "CAACEdEose0cBAEbY2g2chEZC7YTeie06YD08dYrs0bZCIvBn6OMt1Rjc9lGIGWofZA74f1xvLSl30ZAeReHzG9SeZBfAm2ZAIaR1Ms2006DLH98J2CN6oUOLpeNuztjL80nEquLap5I90sGqaHOamYMRryvsiphwJMoxHXWu7svJxB60RLr59t5C1voGeAGsDbz7fBSjbZCiCgcPCHurcz5";
	private static final String APPSECRET = "nKcdsz1Mk4YM9tCCkuqptCAmBuQ";
	private static final String APPID = "1585535958364627";
	
	private static Facebook facebook;
	private ResponseList<Post> feed;
	
	public FacebookTest() throws FacebookException {
		//construct facebook object
		facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(APPID, APPSECRET);
		AccessToken at = new AccessToken(USERTOKEN);
		facebook.setOAuthAccessToken(at);
			   
		//set config builder
	    /** ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
          .setOAuthAccessToken(USERTOKEN)
          .setOAuthAppId(APPID)
	      .setOAuthAppSecret(APPSECRET);
	      */
			      	   
	    //get posts
	    feed = facebook.getHome(new Reading().limit(100));
	}
	
	//get facebook object
	public static Facebook getFacebook() {
		return facebook;
	}
	
	//get a list of posts
	public List<Post> getValidPosts() throws FacebookException{
		return (List<Post>) feed;
	}
	
	//filter out any post whose story contains a keyword
	public void filterFeed() throws FacebookException {
		
	    for (int i = 0; i < feed.size(); i++) {    	
			Post post = feed.get(i);

			String story = post.getStory();
			String type = post.getType();
			if (story != null) {
				boolean canUse = type.equalsIgnoreCase("status");
				//check story for keywords
				for (String key : KEYWORDS) {
					if(story.contains(key)) {
						canUse = false;
					}
				}
				if (!canUse) {
					feed.remove(i);
					//i--;
				}
			}	
	    }
	}
	
	//sort posts by time stamp (most recent first) using shell sort
	public void sortFeed() throws FacebookException {
		int inc = feed.size() / 2;
		while (inc > 0) {
			for (int i = inc; i < feed.size(); i++) {
				int j = i;
				Post temp = feed.get(i);
				while (j >= inc && feed.get(j-inc).getCreatedTime().compareTo(temp.getCreatedTime()) < 0) {
					feed.set(j, feed.get(j-inc));
					j = j-inc;
				}
				feed.set(j, temp);
			}
			if (inc == 2) inc = 1;
			else inc *= (5.0/11);
		}
	}
}
