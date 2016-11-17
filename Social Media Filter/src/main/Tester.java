package main;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PagableList;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

public class Tester {

	public static void main(String[] args) throws FacebookException{
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId("","");
		String accessToken = "CAACEdEose0cBAEbugpnwBEZCbkvkmBiMl"
				+ "QsKW6ziptX73DtWdfbZBQNA8LpQIZBdwLM1oX3mw08EmCA"
				+ "0VADMFAmux6NibcMG2w9QM3Nbu6fJaA1C14BH5xZBVJHI0I"
				+ "W8glgwKUZAhkimJ4CpW3Oh3NblbQb9MS8XkMEhNTVKZAZBeT"
				+ "6WchGqZCAWFQ5ft47DIl5bvIpJKvePcZC4B5OQqNf6v";
		AccessToken at = new AccessToken(accessToken);
		facebook.setOAuthAccessToken(at);
		ResponseList<Post> feeds = facebook.getFeed("143159725854770", new Reading().limit(25));

	        // For all 25 feeds...
	        for (int i = 0; i < feeds.size(); i++) {
	            // Get post.
	            Post post = feeds.get(i);
	            // Get (string) message.
	            String message = post.getMessage();
	                            // Print out the message.
	            System.out.println(message);

	            // Get more stuff...
	            PagableList<Comment> comments = post.getComments();
	            String date = post.getCreatedTime().toString();
	            String name = post.getFrom().getName();
	            String id = post.getId();
	            System.out.println("-----------------------------");
	        }
	}
}
