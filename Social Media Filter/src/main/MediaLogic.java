package main;

import java.util.Date;
import java.util.List;

import twitter4j.Status;
import facebook4j.Post;

public class MediaLogic {
	
	private String author;
	private String content;
	private Date date;
	private String type;
	
	/** 
	public MediaLogic(String author, String content, Date date, String type) {
		this.author = author;
		this.content = content;
		this.date = date;
		this.type = type;
	}
	**/
	
	public void merge(List<Post> fb, List<Status> tw) {
		for(int i = 0; i < fb.size(); i ++) {
			if(fb.get(i).getCreatedTime().compareTo(tw.get(i).getCreatedAt()) < 0) {
				PostGUI.printMedia(fb.get(i));
			}
			else PostGUI.printMedia(tw.get(i));
		}
	}
}
