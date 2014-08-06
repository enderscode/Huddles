package com.luisibanez.huddles.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.luisibanez.huddles.R;

public class SuccessDialog extends DialogFragment 
{
	private String title;
	private String message;
	
    private SuccessDialog(String title, String message) {
		super();
		this.title = title;
		this.message = message;
	}

	public static SuccessDialog newInstance(String title, String desc) 
    {
    		return new SuccessDialog(title, desc);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(R.string.ok,	new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog,	int whichButton) 
					{
						dismiss();
					}
				}).create();
	}
}