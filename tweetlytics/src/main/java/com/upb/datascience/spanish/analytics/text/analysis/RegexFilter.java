package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class RegexFilter extends TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private boolean strict = false;
	
	/*
	 * URL = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
	 * TARGET = "@[a-zA-Z0-9_]{1,15}"
	 * QUESTION = \\?
	 */
	private String regex;
	private String replacement = "<URL>";

	public RegexFilter(TokenStream input, String regex, String replacement) {
		super(input);
		this.regex = regex;
		this.replacement = replacement;
	}
	
	public RegexFilter(TokenStream input, String regex, String replacement, boolean strict) {
		super(input);
		this.regex = regex;
		this.replacement = replacement;
		this.strict = strict;
	}
	
	@Override
	public final boolean incrementToken() throws IOException {
		if (!input.incrementToken()) {
			return false;
		}

		final char[] buffer = termAtt.buffer();
		final int bufferLength = termAtt.length();
		String term = new String(buffer, 0, bufferLength);
		
		if (strict && term.trim().matches(regex)) {
			replaceByTerm(replacement);
			
			if (replacement.trim().equals("")) {
				//this.input.incrementToken();
			}
		} else if (!strict) {
			String newTerm = term.replaceAll(regex, replacement);
			
			if (!term.equals(newTerm)) {
				replaceByTerm(newTerm);
			}
			
			if (newTerm.trim().equals("")) {
				//this.input.incrementToken();
			}
		}

		return true;
	}
	
	private void replaceByTerm(String newTerm) {
		termAtt.setEmpty();
		termAtt.append(newTerm);
	}
}
