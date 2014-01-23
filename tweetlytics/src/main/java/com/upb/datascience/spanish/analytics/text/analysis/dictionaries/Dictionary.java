package com.upb.datascience.spanish.analytics.text.analysis.dictionaries;

public interface Dictionary {

	public boolean contains(String word);
	
	public String getValue(String word);
}
