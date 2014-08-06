package com.luisibanez.huddles.utils;

import java.util.ArrayList;
import java.util.List;

public class ArticleIdValidator {
	
	public static final int MIN_LENGTH_ARTICLE_ID = 1;

	public static List<String> validateArticleId(String id)
	{
		List<String> errors = new ArrayList<String>();
		if (id.length() < MIN_LENGTH_ARTICLE_ID)
		{
			errors.add("Article Id can't be empty");
		}
		return errors;
	}
}
