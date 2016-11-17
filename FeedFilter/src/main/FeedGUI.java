package main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.plaf.OptionPaneUI;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.ResponseList;
import facebook4j.FacebookException;
import facebook4j.Post;


public class FeedGUI {

	private static JFrame frame;
	private static JTextField txtName;
	private static JTextField txtTime;
	private static int prevY = 0;
	private static final int MAX_WIDTH = 1000;
	private static final int MAX_HEIGHT = 1000;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					FeedGUI window = new FeedGUI();
					window.frame.setVisible(true);
					FacebookTest fbTest = new FacebookTest();
					fbTest.filterFeed();
					fbTest.sortFeed();
					TwitterTest twTest = new TwitterTest();
					merge(fbTest.getValidPosts(), twTest.getStatuses());
					System.out.println("Check 14 (out of loop)");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} );
	}

	/**
	 * Create the application.
	 */
	public FeedGUI() throws FacebookException, TwitterException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, MAX_WIDTH, MAX_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		/** JScrollBar bar = new JScrollBar(JScrollBar.HORIZONTAL, MAX_WIDTH - 75, 20, 50, MAX_HEIGHT - 40);
		bar.addAdjustmentListener( new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				repaint();
			}
		});
		frame.getContentPane().add(bar);
		*/
		
		
		/** final JTextArea textArea = new JTextArea(0, 1000);
		JScrollPane scrollBar = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textArea.setText("test data");
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollBar.setBounds(MAX_WIDTH - 60, 10, 20, MAX_HEIGHT - 100);
		frame.getContentPane().add(scrollBar); */
		
		/** JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(10, MAX_HEIGHT - 125, 90, 25);
		frame.getContentPane().add(btnRefresh); */
	}
	
	public static void printPost(Post post) throws FacebookException {
		String name = post.getFrom().getName();
		String text = post.getMessage();
		String time = post.getCreatedTime().toString();
		if(name == null) {
			System.out.println("Check 2 (null name)");
			name = "name not found";
		}
		if(text == null) {
			System.out.println("Check 3 (null text)");
			text = "data not found";
		}
		if(time == null) {
			System.out.println("Check 3 (null time)");
			time = "time not found";
		}
		
		int temp = printPost(name, text, time);
		faceBook(80, temp + 5, 90, 25, post);
		System.out.println("Check 13 (end of main print)");
	}
	
	public static void printPost(Status status) throws TwitterException	{
		if(!status.isRetweet()) {
			String name = status.getUser().getName();
			String text = status.getText();
			String time = status.getCreatedAt().toString();
			if(name == null) {
				System.out.println("Check 2 (null name)");
				name = "name not found";
			}
			if(text == null) {
				System.out.println("Check 3 (null text)");
				text = "data not found";
			}
			if(time == null) {
				System.out.println("Check 3 (null time)");
				time = "time not found";
			}
			
			int temp = printPost(name, text, time);
			
			twitter(80, temp + 5, 90, 25, status);
		}
	}
	
	public static int printPost(String name, String post, String time) {
		
		int nameX1 = 10;
		int nameY1 = prevY + 5;
		int nameX2 = width(name);
		int nameY2 = length(name);
		
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setText(name);
		txtName.setBounds(nameX1, nameY1, nameX2, nameY2);
		txtName.setColumns(10);
		frame.getContentPane().add(txtName);
		System.out.println("Check 4 (name printed)");

		int textX1 = 80;
		int textY1 = nameY1 + 25;
		int textX2 = width(post);
		int textY2 = length(post);
		
		JTextArea postArea = new JTextArea();
		postArea.setText(splitStr(post));
		postArea.setEditable(false);
		postArea.setBounds(textX1, textY1, textX2, textY2);
		frame.getContentPane().add(postArea);
		System.out.println("Check 5 (post printed)");
		
		int timeX1 = textX1;
		int timeY1 = textY1 + textY2;
		int timeX2 = width(time);
		int timeY2 = length(time);
		
		txtTime = new JTextField();
		txtTime.setEditable(false);
		txtTime.setText(splitStr(time));
		txtTime.setBounds(timeX1, timeY1, timeX2, timeY2);
		frame.getContentPane().add(txtTime);
		txtTime.setColumns(10);
		System.out.println("Check 6 (time printed)");
		
		return timeY1 + timeY2;
	}

	private static void faceBook(int x1, int y1, int x2, int y2, Post post) throws FacebookException{		
		
		JButton btnLike = new JButton("Like");
		btnLike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					FacebookTest.getFacebook().likePost(post.getId());
				} catch (FacebookException e) {
					e.printStackTrace();
				}
			}
		});
		btnLike.setBounds(x1, y1, x2, y2);
		frame.getContentPane().add(btnLike);
		System.out.println("Check 11 (like added)");
		
		prevY = y1 + y2;
	}
	
	private static void twitter(int x1, int y1, int x2, int y2, Status status) throws TwitterException {
		
		JButton btnFav = new JButton("Favorite");
		btnFav.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					TwitterTest.getTwitter().createFavorite(status.getId());
				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		});
		btnFav.setBounds(x1, y1, x2, y2);
		frame.getContentPane().add(btnFav);
		
		prevY = y1 + y2;
	}
	
	private static int width(String str) {
		int width = str.length() * 7;
		if(width < MAX_WIDTH - 100) return width;
		return MAX_WIDTH - 100;
	}
	
	private static int length(String str) {
		int numer = str.length() * 7;
		int denom = MAX_WIDTH - 100;
		return ((numer/denom) + 1) * 20;
	}
	
	private static String splitStr(String str) {
		System.out.println("Check 7 (start of split)");
		String splitStr = str;
		int limit = (length(str) / 20);
		for(int i = 1; i < limit; i++) {
			System.out.println("Check 8 (in first split loop)");
			int split = (MAX_WIDTH - 100) / 7 * i; 
			for(int j = split; j > split - 10; j--) {
				System.out.println("Check 9 (in second split loop)");
				if(str.charAt(j) == ' ') {
					split = j;
					j = 0;
				}
				else if(j == 1) {
					str = str.substring(0, split) + "-" + str.substring(split);
				}
			}
			splitStr = str.substring(0, split + 1) + "\n" + str.substring(split + 1);
		}
		return splitStr;
	}
	
	public static void merge(List<Post> posts, List<Status> statuses) throws FacebookException, TwitterException {
		if(posts != null && statuses != null) {
			int postIndex = 0, statusIndex = 0;
			while (postIndex < posts.size() || statusIndex < statuses.size()) {
				if(statusIndex < statuses.size()) {
					printPost(statuses.get(statusIndex));
					statusIndex++;
				}
				if (postIndex < posts.size()) {	
					printPost(posts.get(postIndex));
					postIndex++;
				}
			}
		}
		if(posts == null) {
			for(Status s : statuses) {
				printPost(s);
			}
		}
		if(statuses == null) {
			for(Post p : posts) {
				printPost(p);
			}
		}
	}
	
	public static void merge2(List<Post> posts, List<Status> statuses) throws FacebookException, TwitterException {
		if(posts != null && statuses != null) {
			int postIndex = 0, statusIndex = 0;
			while (postIndex < posts.size() && statusIndex < statuses.size()) {
				Date post = posts.get(postIndex).getCreatedTime();
				Date status = statuses.get(statusIndex).getCreatedAt();
				int comp = post.compareTo(status);
				if(comp < 0) {
					printPost(statuses.get(statusIndex));
					statusIndex++;
				}
				else {
					printPost(posts.get(postIndex));
					postIndex++;
				}
			}
		}
		if(posts == null) {
			for(Status s : statuses) {
				printPost(s);
			}
		}
		if(statuses == null) {
			for(Post p : posts) {
				printPost(p);
			}
		}
	}
}
