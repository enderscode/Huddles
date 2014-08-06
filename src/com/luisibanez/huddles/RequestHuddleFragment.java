package com.luisibanez.huddles;

import java.util.List;

import com.luisibanez.huddles.utils.ArticleIdValidator;
import com.luisibanez.huddles.utils.ErrorDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class RequestHuddleFragment extends Fragment{
	
	private RequestHuddleFragment(){ }
	
	public static RequestHuddleFragment newInstance()
	{
		return new RequestHuddleFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	

		View view = inflater.inflate(R.layout.request_huddle, container, false);
		
		final EditText newsIdEditText = (EditText) view.findViewById(R.id.newsId);
		final Button submitButton = (Button) view.findViewById(R.id.submit);
		
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String articleId = newsIdEditText.getText().toString();
				List<String> error = ArticleIdValidator.validateArticleId(articleId);
				if (!error.isEmpty())
				{
					showErrorDialog("Error", error.get(0));
					return;
				}
				
				Intent intent = new Intent(getActivity().getBaseContext(), HuddlesActivity.class);
				intent.putExtra(HuddlesActivity.NEWS_ID_KEY, articleId);
				startActivity(intent);
			}
		});
		return view;
    }
	
	private void showErrorDialog(String title, String message) {
		ErrorDialog dialog = ErrorDialog.newInstance(title, message, null);
		dialog.show(getFragmentManager(), "errpr");
	}
}
