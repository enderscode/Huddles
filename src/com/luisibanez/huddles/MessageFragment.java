package com.luisibanez.huddles;

import com.luisibanez.huddles.utils.SuccessDialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessageFragment extends Fragment{

private MessageFragment(){ }
	
	public static MessageFragment newInstance()
	{
		return new MessageFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	

		View view = inflater.inflate(R.layout.message_huddle, container, false);
		
		final TextView info = (TextView) view.findViewById(R.id.info);
		
		Bundle extras = getArguments();
		
		if(extras == null){
			//TODO: show error
		} else {
		    String friendList = extras.getString(HuddlesActivity.FRIENDS_KEY);
		    if(friendList == null){
		    		//TODO: show error
		    }else{
		    		info.append(" " + friendList);
		    }
		}
		
		setHasOptionsMenu(true);
		return view;
    }
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
    {
        inflater.inflate(R.menu.menu_message, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
        case R.id.action_send:
        		showSuccessDialog();
            return true;
        default:
            break;
        }
        return false;
    }

	private void showSuccessDialog() {
		SuccessDialog dialog = SuccessDialog.newInstance("Invitation Sent", "Your invitation has been sent correctly");
		dialog.show(getFragmentManager(), "success");
	}
}
