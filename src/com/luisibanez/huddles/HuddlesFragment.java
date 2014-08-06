package com.luisibanez.huddles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luisibanez.huddles.datasource.GetFriendsDatasourceImp;
import com.luisibanez.huddles.datasource.callbacks.GetFriendsCallback;
import com.luisibanez.huddles.datasource.interfaces.GetFriendsDatasource;
import com.luisibanez.huddles.model.User;
import com.luisibanez.huddles.utils.ErrorDialog;
import com.luisibanez.huddles.utils.ErrorDialog.OnErrorDialogClose;
import com.luisibanez.huddles.utils.SuccessDialog;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HuddlesFragment extends Fragment {

	private Map<String, User> users = new HashMap<String, User>();
	private List<String> contentOrderList = new ArrayList<String>();
	private SparseArray<String> titles = new SparseArray<String>();
	private SparseBooleanArray selectedPositions = new SparseBooleanArray();
	
	private HuddleAdapter adapter;
	
	private String articleId;
	private ListView huddlesList;
	private ProgressBar progressBar;
	
	private boolean isDownloadInProgress = false;	
	
	private HuddlesFragment(){ }
	
	public static HuddlesFragment newInstance()
	{
		return new HuddlesFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	

		View view = inflater.inflate(R.layout.huddle_fragment, container, false);
		
		progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		
		huddlesList = (ListView) view.findViewById(R.id.huddles_listView);
	    adapter = new  HuddleAdapter(getActivity());
	    huddlesList.setAdapter(adapter);
	    
	    huddlesList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view != null) {
					CheckBox checkBox = (CheckBox) view.findViewById(R.id.selected);
					checkBox.setChecked(!checkBox.isChecked());
					ActivityCompat.invalidateOptionsMenu(getActivity());
				}
			}
		});
		
		Bundle extras = getArguments();
		
		if(extras == null){
			throw new IllegalArgumentException("The article ID can't be null");
		} else {
			articleId = extras.getString(HuddlesActivity.NEWS_ID_KEY);
		    if(articleId == null){
		    		throw new IllegalArgumentException("The article ID can't be null");
		    }
		    getFriends(articleId);
		}
		
		setRetainInstance(true);
		setHasOptionsMenu(true);
		return view;
    }
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
    {
        inflater.inflate(R.menu.menu_huddle, menu);
        if (selectedPositions.size()<1) {
            menu.findItem(R.id.action_create).setEnabled(false);
        } else {
            menu.findItem(R.id.action_create).setEnabled(true);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
        case R.id.action_create:
        		onCreateClick();
            return true;
        default:
            break;
        }
        return false;
    }

	private void onCreateClick() {
		List<String> nonPrimaryNames = new ArrayList<String>();
		
		for(int i = 0; i < selectedPositions.size() ; i++)
		{
			User user = users.get(contentOrderList.get(selectedPositions.keyAt(i)));
			if(!user.isPrimary()){
				nonPrimaryNames.add(user.getName());
			}
		}
		
		if(nonPrimaryNames.size() < 1){
			showSuccessDialog();
		} else {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append(nonPrimaryNames.get(0));
	        for (int i = 1; i < nonPrimaryNames.size(); i++) {
	            sbuilder.append(", " + nonPrimaryNames.get(i));
	        }
	        sbuilder.append(".");
			
			Intent intent = new Intent(getActivity().getBaseContext(), MessageActivity.class);
			intent.putExtra(HuddlesActivity.FRIENDS_KEY, sbuilder.toString());
			startActivity(intent);
		}
		
	}
	
	private void getFriends(final String newsId) {
		if (!isDownloadInProgress) {
			isDownloadInProgress = true;

			progressBar.setVisibility(View.VISIBLE);

			GetFriendsDatasource friendsDatasource = new GetFriendsDatasourceImp();
			 
			friendsDatasource.getFriends(articleId, new GetFriendsCallback() {
				 
				@Override
				public void friendsReady(Map<String, User> friends,
						List<String> contentOrderList,
						SparseArray<String> titles) {
					if(friends == null || friends.isEmpty()){
						showErrorDialog("Network Error", "There was an error connecting with the server, please try again later", null);
					}
					HuddlesFragment.this.users = friends;
		 			HuddlesFragment.this.contentOrderList=contentOrderList;
		 			HuddlesFragment.this.titles = titles;
		 			adapter.notifyDataSetChanged();
		 			progressBar.setVisibility(View.GONE);
		 			isDownloadInProgress = false;
						
				}
			}); 	    
	    }
	}
	
	private void showSuccessDialog() {
		SuccessDialog dialog = SuccessDialog.newInstance("Huddle created!", "You can start talking with your friends");
		dialog.show(getFragmentManager(), "success");
	}
	
	private void showErrorDialog(String title, String message, OnErrorDialogClose callback) {
		ErrorDialog dialog = ErrorDialog.newInstance(title, message, callback);
		dialog.show(getFragmentManager(), "success");
	}
	
	private class HuddleAdapter extends BaseAdapter {
		private static final int TYPE_USER = 0;
		private static final int TYPE_SECTION = 1;
		private static final int TYPES_COUNT = 2;

		private LayoutInflater inflater;

		public HuddleAdapter(Context context) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getItemViewType(int position) {
			return contentOrderList.get(position).equals("-1") ? TYPE_SECTION : TYPE_USER;
		}

		@Override
		public int getViewTypeCount() {
			return TYPES_COUNT;
		}

		@Override
		public int getCount() {
			return contentOrderList.size();
		}

		@Override
		public String getItem(int position) {
			return contentOrderList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return getItemViewType(position) == TYPE_USER;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			int rowType = getItemViewType(position);

			switch (rowType) {
			case TYPE_USER:
				UserViewHolder userHolder = null;
				if (convertView == null) {
					userHolder = new UserViewHolder();
					convertView = inflater.inflate(R.layout.huddle_row, parent, false);
					userHolder.selected = (CheckBox) convertView	.findViewById(R.id.selected);
					userHolder.selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							int getPosition = (Integer) buttonView.getTag();  
							if(buttonView.isChecked())
							{
								selectedPositions.put(getPosition, true);
							}else{
								selectedPositions.delete(getPosition);
							}
						}
					});
					userHolder.userImage = (ImageView) convertView.findViewById(R.id.user_image);
					userHolder.name = (TextView) convertView.findViewById(R.id.name);
					userHolder.team = (TextView) convertView.findViewById(R.id.team);
					userHolder.chatFlag = (ImageView) convertView.findViewById(R.id.chat_flag);
					convertView.setTag(userHolder);
				} else {
					userHolder = (UserViewHolder) convertView.getTag();
				}
				
				User user = users.get(contentOrderList.get(position));
				Picasso.with(getActivity()).load(user.getImage()).into(userHolder.userImage);
				userHolder.name.setText(user.getName());
				if(user.getTeam() != null){
					
					String likesText = getString(R.string.team_likes);
					userHolder.team.setText(likesText + " " +user.getTeam());
					userHolder.team.setVisibility(View.VISIBLE);
				}else{
					userHolder.team.setVisibility(View.GONE);
				}
				
				userHolder.selected.setTag(position);
				userHolder.selected.setChecked(selectedPositions.get(position));
				
				int visibility = user.isPrimary() ? View.VISIBLE : View.GONE;
				userHolder.chatFlag.setVisibility(visibility);
				
				if(visibility == View.VISIBLE)
				{
					int resource = user.isChat() ? R.drawable.green_icon : R.drawable.grey_icon;
					Picasso.with(getActivity()).load(resource).into(userHolder.chatFlag);
				} 
				break;
			case TYPE_SECTION:
				SectionViewHolder sectionHolder = null;
				if (convertView == null) {
					sectionHolder = new SectionViewHolder();
					convertView = inflater.inflate(R.layout.huddle_section_row, parent, false);
					sectionHolder.sectionTitle = (TextView) convertView.findViewById(R.id.section_text);
					convertView.setTag(sectionHolder);
				} else {
					sectionHolder = (SectionViewHolder) convertView.getTag();
				}
				sectionHolder.sectionTitle.setText(titles.get(position));
				break;
			}
			return convertView;
		}
	}
	
	public static class UserViewHolder {
		public CheckBox selected;
		public ImageView userImage;
		public TextView name;
		public TextView team;
		public ImageView chatFlag;
	}

	public static class SectionViewHolder {
		public TextView sectionTitle;
	}
}
