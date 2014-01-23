package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.IOException;
import java.text.Normalizer;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class DiacriticFilter extends TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

	public DiacriticFilter(TokenStream input) {
		super(input);
	}

	@Override
	public final boolean incrementToken() throws IOException {
		if (!input.incrementToken()) {
			return false;
		}

		final char[] buffer = termAtt.buffer();
		final int bufferLength = termAtt.length();
		String term = new String(buffer, 0, bufferLength);

		replaceByTerm(Normalizer.normalize(term, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));

		return true;
	}

	private void replaceByTerm(String newTerm) {
		termAtt.setEmpty();
		termAtt.append(newTerm);
	}
}
