package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.FlagsAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Dictionary;

public class DictionaryFilter extends TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final FlagsAttribute flagsAtt = addAttribute(FlagsAttribute.class);
	private final PositionIncrementAttribute posIncAtt = addAttribute(PositionIncrementAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	private final PayloadAttribute payloadAtt = addAttribute(PayloadAttribute.class);

	private Dictionary dictionary;

	private final Token wrapper = new Token();
	protected final LinkedList<Token> tokens;

	public DictionaryFilter(TokenStream input, Dictionary dictionary) {
		super(input);

		this.dictionary = dictionary;
		tokens = new LinkedList<Token>();
	}

	private final void setToken(final Token token) throws IOException {
		clearAttributes();
		termAtt.copyBuffer(token.buffer(), 0, token.length());
		flagsAtt.setFlags(token.getFlags());
		typeAtt.setType(token.type());
		offsetAtt.setOffset(token.startOffset(), token.endOffset());
		posIncAtt.setPositionIncrement(token.getPositionIncrement());
		payloadAtt.setPayload(token.getPayload());
	}

	@Override
	public final boolean incrementToken() throws IOException {
		if (tokens.size() > 0) {
			setToken(tokens.removeFirst());
			return true;
		}

		if (!input.incrementToken())
			return false;

		wrapper.copyBuffer(termAtt.buffer(), 0, termAtt.length());
		wrapper.setStartOffset(offsetAtt.startOffset());
		wrapper.setEndOffset(offsetAtt.endOffset());
		wrapper.setFlags(flagsAtt.getFlags());
		wrapper.setType(typeAtt.type());
		wrapper.setPositionIncrement(posIncAtt.getPositionIncrement());
		wrapper.setPayload(payloadAtt.getPayload());

		decompose(wrapper);

		if (tokens.size() > 0) {
			setToken(tokens.removeFirst());
			return true;
		} else {
			return false;
		}
	}

	protected final Token createToken(final int offset, final int length,
			final Token prototype) {
		int newStart = prototype.startOffset() + offset;
		Token t = prototype.clone(prototype.buffer(), offset, length, newStart,
				newStart + length);
		t.setPositionIncrement(0);

		return t;
	}

	protected final Token createToken(final String token) {
		Token t = new Token(token, 0, token.length());
		t.setPositionIncrement(0);

		return t;
	}

	protected void decompose(final Token token) {
		// In any case we give the original token back
		String term = new String(token.buffer(), 0, token.length());

		String value = dictionary.getValue(term);
		String[] terms;

		if (value != null) {
			terms = value.split(" ");
			
			for (int i = 0; i < terms.length; i++) {
				if (!terms[i].trim().equals("")) {
					tokens.add(createToken(terms[i]));
				}
			}
		} else {
			tokens.add(token);
		}
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		tokens.clear();
	}
}
