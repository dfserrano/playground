package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.Reader;
import java.io.StringReader;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.upb.datascience.spanish.analytics.text.analysis.EmptyFilter;
import com.upb.datascience.spanish.analytics.text.analysis.RegexFilter;

public class EmptyFilterTest {

	@Test
	public void test() {
		try {
			String text = "A token will be deleted";
			int expectedNumTokens = 4;

			Analyzer analyzer = new EmptyAnalyzer();

			Reader reader = new StringReader(text);
			TokenStream ts = analyzer.tokenStream(null, reader);

			int count = 0;
			while (ts.incrementToken()) {
				count++;
			}

			// Expects only 4 tokens, since token is erased
			Assert.assertEquals(expectedNumTokens, count);

			analyzer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class EmptyAnalyzer extends Analyzer {

	public TokenStream tokenStream(String fieldName, Reader reader) {
		Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_36, reader);
		TokenFilter regexFilter = new RegexFilter(tokenizer, "token", "", true);
		TokenFilter emptyFilter = new EmptyFilter(regexFilter);

		return emptyFilter;
	}
}
