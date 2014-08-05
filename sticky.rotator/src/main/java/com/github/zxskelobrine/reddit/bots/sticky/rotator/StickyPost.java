package com.github.zxskelobrine.reddit.bots.sticky.rotator;

import com.cd.reddit.json.mapping.RedditLink;

public class StickyPost {

	private String postID;
	private String title;
	private boolean isStickied;
	private RedditLink postDetails;

	public StickyPost(RedditLink postLink) {
		this.postDetails = postLink;
		this.postID = "t3_" + postDetails.getId();
		this.title = postDetails.getTitle();
		this.isStickied = false;
	}

	@Override
	public String toString() {
		return title + " (" + postID + ")";
	}

	/**
	 * @return the postID
	 */
	public String getPostID() {
		return postID;
	}

	/**
	 * @param postID
	 *            the postID to set
	 */
	public void setPostID(String postID) {
		this.postID = postID;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the isStickied
	 */
	public boolean isStickied() {
		return isStickied;
	}

	/**
	 * @param isStickied
	 *            the isStickied to set
	 */
	public void setStickied(boolean isStickied) {
		this.isStickied = isStickied;
	}

}
