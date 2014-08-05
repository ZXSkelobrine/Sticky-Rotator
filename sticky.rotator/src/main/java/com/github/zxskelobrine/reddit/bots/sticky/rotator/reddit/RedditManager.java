package com.github.zxskelobrine.reddit.bots.sticky.rotator.reddit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cd.reddit.Reddit;
import com.cd.reddit.RedditException;
import com.cd.reddit.http.util.RedditApiParameterConstants;
import com.cd.reddit.http.util.RedditApiResourceConstants;
import com.cd.reddit.http.util.RedditRequestInput;
import com.cd.reddit.http.util.RedditRequestResponse;
import com.cd.reddit.json.jackson.RedditJsonParser;
import com.cd.reddit.json.mapping.RedditLink;
import com.github.zxskelobrine.reddit.bots.sticky.rotator.StickyPost;

public class RedditManager {

	private static Reddit reddit;
	private static StickyPost currentSticky;

	public static void configureReddit(String username, char[] password) throws RedditException {
		reddit = new Reddit("ProjectAwesome Sticky Rotator/0.1");
		reddit.login(username, new String(password));
		password = null;
	}

	private static RedditJsonParser setPostAsSticky(StickyPost post, String value) throws RedditException {
		final List<String> pathSegments = new ArrayList<String>(2);
		final Map<String, String> form = new HashMap<String, String>(3);

		pathSegments.add(RedditApiResourceConstants.API);
		pathSegments.add("set_subreddit_sticky");

		form.put(RedditApiParameterConstants.API_TYPE, RedditApiParameterConstants.JSON);
		form.put(RedditApiParameterConstants.ID, post.getPostID());
		form.put("state", value);

		final RedditRequestInput requestInput = new RedditRequestInput(pathSegments, null, form);

		final RedditRequestResponse response = reddit.getRequestor().executePost(requestInput);

		final RedditJsonParser parser = new RedditJsonParser(response.getBody());

		return parser;
	}

	public static RedditJsonParser stickyPost(StickyPost post) throws RedditException {
		if (currentSticky != null) unstickyPost(currentSticky);
		currentSticky = post;
		return setPostAsSticky(post, Boolean.toString(true));
	}

	private static RedditJsonParser unstickyPost(StickyPost post) throws RedditException {
		return setPostAsSticky(post, Boolean.toString(false));
	}

	public static List<StickyPost> getSubredditListing(String subreddit) throws RedditException {
		List<StickyPost> posts = new ArrayList<StickyPost>();
		List<RedditLink> links = reddit.listingFor(subreddit, RedditApiResourceConstants.NEW);
		for (RedditLink link : links) {
			posts.add(new StickyPost(link));
		}
		return posts;
	}

	public static StickyPost[] getArrayFromList(List<StickyPost> list) {
		StickyPost[] posts = new StickyPost[list.size()];
		posts = list.toArray(posts);
		return posts;
	}

}
