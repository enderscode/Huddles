package com.luisibanez.huddles.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.luisibanez.huddles.R;

public class ErrorDialog extends DialogFragment 
{
	private String title;
	private String message;
	private OnErrorDialogClose callback;
	
    public ErrorDialog(String title, String message, OnErrorDialogClose callback) {
		super();
		this.title = title;
		this.message = message;
		this.callback = callback;
	}

	public static ErrorDialog newInstance(String title, String desc, OnErrorDialogClose callback) 
    {
    		return new ErrorDialog(title, desc, callback);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(message)
				.setNegativeButton(R.string.close,	new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog,	int whichButton) 
					{
						dismiss();
					}
				}).create();
	}
    
    @Override
    public void onDismiss(DialogInterface dialog) {
    		if(callback != null){
    			callback.onCloseDialog();
    		}
	    	super.onDismiss(dialog);
    }
    
    public interface OnErrorDialogClose
    {
    		void onCloseDialog();
    }
}
