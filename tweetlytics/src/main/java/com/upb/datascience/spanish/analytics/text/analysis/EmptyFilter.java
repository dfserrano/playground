package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

public class EmptyFilter extends TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);

	public EmptyFilter(TokenStream input) {
		super(input);
	}

	@Override
	public final boolean incrementToken() throws IOException {
		// return the first non-stop word found
		int skippedPositions = 0;
		while (input.incrementToken()) {
			final char[] buffer = termAtt.buffer();
			final int bufferLength = termAtt.length();
			String term = new String(buffer, 0, bufferLength);

			if (!term.trim().equals("")) {
				posIncrAtt.setPositionIncrement(posIncrAtt
						.getPositionIncrement() + skippedPositions);
				return true;
			}
			skippedPositions += posIncrAtt.getPositionIncrement();
		}
		// reached EOS -- return false
		return false;
	}
}
