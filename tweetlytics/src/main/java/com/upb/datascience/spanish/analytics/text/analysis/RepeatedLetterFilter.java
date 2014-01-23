package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class RepeatedLetterFilter extends TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private Pattern threeLetterPattern;

	public RepeatedLetterFilter(TokenStream input) {
		super(input);

		threeLetterPattern = Pattern.compile("([A_Za-z])\\1{2,}");
	}

	@Override
	public final boolean incrementToken() throws IOException {
		if (!input.incrementToken()) {
			return false;
		}

		final char[] buffer = termAtt.buffer();
		final int bufferLength = termAtt.length();
		String term = new String(buffer, 0, bufferLength);

		normalizeTerm(term);

		return true;
	}

	private void normalizeTerm(String term) {
		Matcher matcher = threeLetterPattern.matcher(term);
		int fromIndex = 0;
		
		while (matcher.find(fromIndex)) {
			String group = matcher.group();
			
			String replacement = group.substring(0, 3);
			term = term.replaceFirst(group, replacement);
			
			fromIndex = matcher.start() + 3;
			
			matcher = threeLetterPattern.matcher(term);
		}

		termAtt.setEmpty();
		termAtt.append(term);
	}
}
