package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.Reader;
import java.io.StringReader;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.upb.datascience.spanish.analytics.text.analysis.RepeatedLetterFilter;

public class RepeatedLetterFilterTest {

	@Test
	public void test() {
		try {
			String text = "A token will be coooooool or cool or aaaaabaaa";
			int expectedNumTokens = 9;

			Analyzer analyzer = new RepeatedLetterAnalyzer();

			Reader reader = new StringReader(text);
			TokenStream ts = analyzer.tokenStream(null, reader);
			CharTermAttribute charTermAttribute = ts
					.addAttribute(CharTermAttribute.class);

			int count = 0;
			while (ts.incrementToken()) {
				count++;
				
				if (count == 5) {
					String term = charTermAttribute.toString();
					Assert.assertEquals("coool", term);
				}
				
				if (count == 7) {
					String term = charTermAttribute.toString();
					Assert.assertEquals("cool", term);
				}
				
				if (count == 9) {
					String term = charTermAttribute.toString();
					Assert.assertEquals("aaabaaa", term);
				}
			}

			// Expects only 4 tokens, since token is erased
			Assert.assertEquals(expectedNumTokens, count);

			analyzer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class RepeatedLetterAnalyzer extends Analyzer {

	public TokenStream tokenStream(String fieldName, Reader reader) {
		Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_36, reader);
		TokenFilter repeatedLetterFilter = new RepeatedLetterFilter(tokenizer);

		return repeatedLetterFilter;
	}
}
