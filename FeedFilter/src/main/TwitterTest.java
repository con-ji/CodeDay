package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterTest {
	
	private static final String CONKEY = "KwNtGX7fIAtqqWUgHgbEdjFXW";
	private static final String CONSEC = "0CnKwDBV72Q85sE8mWCXFC5mIBfTJAgKRTXhAiaCVH57sVmWzF";
	private static final String ACCTOK = "1146313718-357rkySbgnmLvfP0jf2kkGQbcnNxfhTZ66g7Pri";
	private static final String ACCTOKSEC = "LpMwcoHHJkfF6kWI0HRADbVOw6RgW3XHkLFPMuqpIFzLl";
	private static ResponseList<Status> statuses;
	
	// URL for authentication
	private static String url;
	
	// Use .getToken() and/or .getTokenSecret() if necessary
	private static RequestToken requestToken;
	
	// Twitter?
	private static Twitter twitter;
	
	// Constructor to input the consumer key and secret
		public TwitterTest() throws TwitterException {
			// Creates a config builder for the TwitterFactory with conKey, conSec
			ConfigurationBuilder cb = new ConfigurationBuilder();
			/**
			cb.setDebugEnabled(true)
				.setOAuthConsumerKey(CONKEY)
				.setOAuthConsumerSecret(CONSEC)
				.setOAuthAccessToken(null)
	            .setOAuthAccessTokenSecret(null);;
	            */
			// TwitterFactory twit = new TwitterFactory(cb.build());
			twitter = TwitterFactory.getSingleton();
			// requestToken = tw.getOAuthRequestToken();
			twitter.setOAuthConsumer(CONKEY, CONSEC);
			AccessToken at = new AccessToken(ACCTOK, ACCTOKSEC);
			twitter.setOAuthAccessToken(at);
			statuses = twitter.getHomeTimeline();
		}
	
	// Main method
	public static void main(String[] args) throws TwitterException, IOException {
	
		// Creates a config builder for the TwitterFactory with conKey, conSec
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
			.setOAuthConsumerKey(CONKEY)
			.setOAuthConsumerSecret(CONSEC);
		
		// A bunch of try-catches
		// First: Makes sure that the program is running, catches if it isn't
		try {
			System.out.println("-----------");
			TwitterFactory twit = new TwitterFactory(cb.build());
			Twitter tw = twit.getInstance();
			
			// Next: Tests to see if it can get a timeline, else throws an exception
			try {
				// Gets a request token, given tw
				requestToken = tw.getOAuthRequestToken();
				System.out.println("Got request token.");
				System.out.println("Request token: " + requestToken.getToken());
				System.out.println("Request token secret: " + requestToken.getTokenSecret());
				System.out.println("---------");

				// Currently has request token, secret
				// BufferedReader to read user PIN input - REPLACE WITH GUI
				AccessToken accessToken = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
				// While there is no accessToken, ask for one
				while (null == accessToken) {
					// Creates an authorization URL for the user to give their permission and PIN
					System.out.println("Open the following URL and grant access to your account:");
					url = requestToken.getAuthorizationURL();
					System.out.println(url);
					System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
					String pin = br.readLine();
            
					// Tests to see if the PIN is valid, else, throws an exception
					try {
						if (pin.length() > 0) {
							// Token becomes the one accessed with the PIN
							accessToken = tw.getOAuthAccessToken(requestToken, pin);
						} else {
							// Token becomes a default one, requests token
							accessToken = tw.getOAuthAccessToken(requestToken);
						}
					} catch (TwitterException te) {
						if (401 == te.getStatusCode()) {
							System.out.println("Unable to get the access token.");
						} else {
							te.printStackTrace();
						}
					}
				}
			
				// Success!
				System.out.println("Got access token.");
				System.out.println("Access token: " + accessToken.getToken());
				System.out.println("Access token secret: " + accessToken.getTokenSecret());
				System.out.println();
            
			} catch (IllegalStateException ie) {
					// Access token is already available, or conKey/conSec is not set.
					if (!tw.getAuthorization().isEnabled()) {
						System.out.println("OAuth consumer key/secret is not set.");
						System.exit(-1);
					}
				}
			
				// Prints out, of the 1st 20 posts on the timeline, the non-retweets, separated with lines
				statuses = tw.getHomeTimeline();
				for (Status status : statuses) {
					if(!status.isRetweet()) {
						System.out.println(status.getUser().getName() + ":" + status.getText());
						System.out.println("-----------");
					}
				}
				
			} catch(TwitterException te) {
				te.printStackTrace();
				System.out.println("Failed to get timeline: " + te.getMessage());
				System.exit(-1);
     	  
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.out.println("Failed to read the system input.");
				System.exit(-1);
		}
	}
	
	/**
	public List<Media> toMedia(List<Status> statuses) {
		List<Media> m = new ArrayList<Media>(0);
		for(Status s : statuses) {
			Media med = new Media(s.getUser().getName(),
								  s.getText(),
								  s.getCreatedAt(),
								  "tw");
			m.add(med);
		}
		return m;
	}
	**/
	
	public List<Status> getStatuses() {
		return (List<Status>)statuses;
	}
	
	public String getURL() {
		return url;
	}
	
	public RequestToken getRToken() {
		return requestToken;
	}

	public static Twitter getTwitter() {
		return twitter;
	}
}
