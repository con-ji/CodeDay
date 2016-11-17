package main;

import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;

import twitter4j.Status;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.api.PostMethods;


public class FeedGUIOld {

	private static final FacebookTest FBTEST = new FacebookTest();
	
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
			public void run() {
				try {
					FeedGUI window = new FeedGUI();
					FeedGUI.frame.setVisible(true);
					printPost("Jason Ji", "We will focus our feed to better suit you. Too bad it", "5/23/15");
					printPost("Brian Vickers", "We will get that shit out of your feed", "5/23/15");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FeedGUI() {
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
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(MAX_WIDTH - 60, 10, 20, MAX_HEIGHT - 100);
		frame.getContentPane().add(scrollBar);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(10, MAX_HEIGHT - 125, 90, 25);
		frame.getContentPane().add(btnRefresh);
	}
	
	public static void printPost(Post post) throws FacebookException {
		String name = post.getFrom().getName();
		String text = post.getMessage();
		String time = post.getCreatedTime().toString();
		
		faceBook(80, printPost(name, text, time, post) + 5, 90, 25);
	}
	
	public static void printPost(Status status)	{
		String name = status.getUser().getName();
		String text = status.getText();
		String time = status.getCreatedAt().toString();
		
		twitter(80, printPost(name, text, time) + 5, 90, 25);
	}
	
	public static int printPost(String name, String post, String time) {
		
		int nameX1 = 10;
		int nameY1 = prevY + 5;
		int nameX2 = width(name, 6);
		int nameY2 = length(name, 6);
		
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setText(splitStr(name, 6));
		txtName.setBounds(nameX1, nameY1, nameX2, nameY2);
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);

		int textX1 = 80;
		int textY1 = nameY1 + 25;
		int textX2 = width(post, 6);
		int textY2 = length(post, 6);
		
		JTextArea postArea = new JTextArea();
		postArea.setText(splitStr(post, 6));
		postArea.setEditable(false);
		postArea.setBounds(textX1, textY1, textX2, textY2);
		frame.getContentPane().add(postArea);
		
		int timeX1 = textX1;
		int timeY1 = textY1 + textY2;
		int timeX2 = width(time, 7);
		int timeY2 = length(time, 7);
		
		txtTime = new JTextField();
		txtTime.setEditable(false);
		txtTime.setText(splitStr(time, 7));
		txtTime.setBounds(timeX1, timeY1, timeX2, timeY2);
		frame.getContentPane().add(txtTime);
		txtTime.setColumns(10);
		
		faceBook(80, timeY1 + timeY2 + 5, 90, 25, post);
		return timeY1 + timeY2;
	}

	private static void faceBook(int x1, int y1, int x2, int y2, Post post) {		
		int likeX1 = x1; 
		int likeY1 = y1;
		int likeX2 = x2;
		int likeY2 = y2;
		
		JButton btnLike = new JButton("Like");
		btnLike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacebookTest.getFacebook.likePost(post.getId());
			}
		});
		btnLike.setBounds(likeX1, likeY1, likeX2, likeY2);
		frame.getContentPane().add(btnLike);
		
		int commentX1 = x1 + 100; 
		int commentY1 = y1;
		int commentX2 = x2; 
		int commentY2 = y2;
		
		JButton btnComment = new JButton("Comment");
		btnComment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacebookTest.getFacebook.commentPost(post.getId(), "test");
			}
		});
		btnComment.setBounds(commentX1, commentY1, commentX2, commentY2);
		frame.getContentPane().add(btnComment);
		
		prevY = commentY1 + commentY2;
	}
	
	private static void twitter(int x1, int y1, int x2, int y2) {
		int likeX1 = x1; 
		int likeY1 = y1;
		int likeX2 = x2;
		int likeY2 = y2;
		
		JButton btnLike = new JButton("Favorite");
		btnLike.setBounds(likeX1, likeY1, likeX2, likeY2);
		frame.getContentPane().add(btnLike);
		
		prevY = y1 - y2;
	}
	
	private static int width(String str, int mult) {
		int width = str.length() * mult;
		if(width < MAX_WIDTH - 70) return width;
		return MAX_WIDTH - 70;
	}
	
	private static int length(String str, int mult) {
		return ((str.length() * mult / (MAX_WIDTH - 70)) + 1) * 15;
	}
	
	private static String splitStr(String str, int mult) {
		String splitStr = str;
		for(int i = 1; i < length(str, mult) / 15; i++) {
			int split = (MAX_WIDTH - 70) / mult * i; 
			for(int j = split; j > 0; j--) {
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
}