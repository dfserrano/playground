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

import com.upb.datascience.spanish.analytics.text.analysis.RegexFilter;

public class RegexFilterTest {

	@Test
	public void test() {
		try {
			String text = "A token will be changed";
			int expectedNumTokens = 5;

			Analyzer analyzer = new RegexAnalyzer();

			Reader reader = new StringReader(text);
			TokenStream ts = analyzer.tokenStream(null, reader);
			CharTermAttribute charTermAttribute = ts
					.addAttribute(CharTermAttribute.class);

			int count = 0;
			while (ts.incrementToken()) {
				count++;

				if (count == 2) {
					String term = charTermAttribute.toString();
					Assert.assertEquals("REPLACED", term);
				}
			}

			Assert.assertEquals(expectedNumTokens, count);

			analyzer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class RegexAnalyzer extends Analyzer {

	public TokenStream tokenStream(String fieldName, Reader reader) {
		Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_36, reader);
		TokenFilter regexFilter = new RegexFilter(tokenizer, "token",
				"REPLACED", true);

		return regexFilter;
	}
}
