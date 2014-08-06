package com.luisibanez.huddles.datasource.callbacks;

import java.util.List;
import java.util.Map;

import android.util.SparseArray;

import com.luisibanez.huddles.model.User;

public interface GetFriendsCallback {
	public void friendsReady(Map<String, User> friends,
				List<String> contentOrderList,
				SparseArray<String> titles);
}
