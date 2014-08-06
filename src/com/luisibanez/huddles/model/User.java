package com.luisibanez.huddles.model;

import static com.luisibanez.huddles.utils.JsonUtils.optBoolean;
import static com.luisibanez.huddles.utils.JsonUtils.optString;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	
	private String id;
	private String name;
	private String image;
	private boolean primary;
	private String facebookId;
	private boolean chat;
	private String team;
	
	public User(String id, String image, String name, String team, boolean primary, String facebookId, boolean chat) {
		this.id = id;
		this.image = image;
		this.name = name;
		this.team = team;
		this.primary = primary;
		this.chat = chat;
		this.facebookId = facebookId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return image;
	}

	public boolean isPrimary() {
		return primary;
	}

	public String getFacebookid() {
		return facebookId;
	}

	public boolean isChat() {
		return chat;
	}

	public String getTeam() {
		return team;
	}
	
	public static class UserBuilder
	{
		private static final String JSON_USER_ID = "id";
		private static final String JSON_USERNAME = "name";
		private static final String JSON_IMAGE = "image";
		private static final String JSON_PRIMARY = "primary";
		private static final String JSON_FACEBOOK_ID = "facebookid";
		private static final String JSON_CHAT = "chat";
		
		private String id;
		private String name;
		private String image;
		private boolean primary;
		private String facebookId;
		private boolean chat;
		private String team;
		
		public UserBuilder(JSONObject json) throws JSONException
		{			
			id = optString(json, JSON_USER_ID);
			name = optString(json, JSON_USERNAME);
			image = optString(json, JSON_IMAGE);
			primary = optBoolean(json, JSON_PRIMARY);
			facebookId = optString(json, JSON_FACEBOOK_ID);
			chat = optBoolean(json, JSON_CHAT);
		}
		
		public UserBuilder setTeam(String team) {
		      this.team = team;
		      return this;
		   }
		
		public User build()
		{
			return new User(id, image, name, team, primary, facebookId, chat);
		}
	}
}
