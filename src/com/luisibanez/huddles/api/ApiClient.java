package com.luisibanez.huddles.api;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public class ApiClient {
	
    private static ApiInterface apiService;

    public static ApiInterface getApiClient() {
        if (apiService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.fanatix.com/app-api/1.5/news/item-friends")
                    .build();

            apiService = restAdapter.create(ApiInterface.class);
        }

        return apiService;
    }

    public interface ApiInterface {
        @GET("/stream/list.json")
        void getFriends(@Query("app-id") String appId, 
        		@Query("app-version") String appVersion, 
        		@Query("app-platform") String appPlatform, 
        		@Query("include-all") boolean includeAll, 
        		@Query("itemid") String itemId, 
        		@Query("auth-fanatix-id") String fanatixId, 
        		@Query("auth-fanatix-token") String fanatixToken, 
        		Callback<Response> callback);
    }
}