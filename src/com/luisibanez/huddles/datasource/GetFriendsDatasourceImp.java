package com.luisibanez.huddles.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.SparseArray;

import com.luisibanez.huddles.api.ApiClient;
import com.luisibanez.huddles.datasource.callbacks.GetFriendsCallback;
import com.luisibanez.huddles.datasource.interfaces.GetFriendsDatasource;
import com.luisibanez.huddles.model.User;
import com.luisibanez.huddles.model.User.UserBuilder;
import com.luisibanez.huddles.utils.JsonUtils;

public class GetFriendsDatasourceImp implements GetFriendsDatasource{
	
	public static final String APP_ID = "cos-iphone";
	public static final String APP_VERSION = "1.2.3AT";
	public static final String APP_PLATFORM = "android";
	public static final boolean INCLUDE_ALL = true;
	public static final String FANATIX_ID = "50f82e1d4a8b519d6d000069";
	public static final String FANATIX_TOKEN = "5fd203caf74e219f585067338b5afae3"; 
	
	private Map<String, User> users = new HashMap<String, User>();
	private List<String> contentOrderList = new ArrayList<String>();
	private SparseArray<String> titles = new SparseArray<String>();
	
	@Override
	public void getFriends(String newsId, final GetFriendsCallback callback) {
		ApiClient.getApiClient().getFriends(
				APP_ID, 
				APP_VERSION, 
				APP_PLATFORM, 
				INCLUDE_ALL, 
				newsId, 
				FANATIX_ID, 
				FANATIX_TOKEN, 
				new Callback<Response>() {
					
					@Override
					public void success(Response response, Response arg1) {
						
						List<String> keyList = new ArrayList<String>();
						try{
							JSONObject jsonObject = JsonUtils.getJsonFrom(response).getJSONObject("response");
							Iterator<?> keys = jsonObject.keys();
							while( keys.hasNext() ){
								keyList.add((String)keys.next());
							}
							
							boolean containsOther = false;
							boolean containsAll = false;
							
							if(keyList.contains("other")){
								keyList.remove("other");
								containsOther = true;
							}
							
							if(keyList.contains("all")){
								keyList.remove("all");
								containsAll = true;
							}
							
							if(keyList.size() > 0){
								titles.put(0, "Recommended");
								contentOrderList.add("-1");
								for(int keyIndex = 0 ; keyIndex<keyList.size() ; keyIndex++){
									String key = keyList.get(keyIndex);
									populateJsonData(jsonObject, key, true);
								}
							}
							
							if(containsOther){
								titles.put(contentOrderList.size(), "Other");
								contentOrderList.add("-1");
								populateJsonData(jsonObject, "other", false);
							}
							
							if(containsAll){
								titles.put(contentOrderList.size(), "All");
								contentOrderList.add("-1");
								populateJsonData(jsonObject, "all", false);
							}
							
							callback.friendsReady(users, contentOrderList, titles);
						}catch (JSONException e) {
							// TODO: handle exception
						}
					}
					
					@Override
					public void failure(RetrofitError error) {
						callback.friendsReady(null, null, null);
					}
				}); 	 			 
		
	} 
	
	private void populateJsonData(JSONObject jsonObject, String key, boolean setTeam) throws JSONException
	{
		if( jsonObject.get(key) instanceof JSONArray ){
			JSONArray jsonArray = (JSONArray) jsonObject.get(key);
			for(int i = 0 ; i < jsonArray.length() ; i++)
			{
				UserBuilder userBuilder =  new UserBuilder((JSONObject)jsonArray.get(i));
				if(setTeam){
					userBuilder.setTeam(key);
				}
				User user = userBuilder.build();
				users.put(user.getId(), user);
				contentOrderList.add(user.getId());
			}	
	    }
	}
}
