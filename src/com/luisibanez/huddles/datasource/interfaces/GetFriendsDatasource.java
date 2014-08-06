package com.luisibanez.huddles.datasource.interfaces;

import com.luisibanez.huddles.datasource.callbacks.GetFriendsCallback;

public interface GetFriendsDatasource {
	void getFriends(String newsId, GetFriendsCallback callback);
}
