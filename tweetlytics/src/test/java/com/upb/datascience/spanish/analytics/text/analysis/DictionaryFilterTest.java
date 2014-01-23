package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.upb.datascience.spanish.analytics.text.analysis.DictionaryFilter;
import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Acronyms;
import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Dictionary;

public class DictionaryFilterTest {

	@Test
	public void test() {
		try {
			String text = "A token will be changed";
			int expectedNumTokens = 6;

			Analyzer analyzer = new CustomDictionaryAnalyzer();

			Reader reader = new StringReader(text);
			TokenStream ts = analyzer.tokenStream(null, reader);
			CharTermAttribute charTermAttribute = ts
					.addAttribute(CharTermAttribute.class);

			int count = 0;
			while (ts.incrementToken()) {
				count++;

				if (count == 2) {
					String term = charTermAttribute.toString();
					Assert.assertEquals("two", term);
				}
			}

			Assert.assertEquals(expectedNumTokens, count);

			analyzer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class CustomDictionaryAnalyzer extends Analyzer {

	public TokenStream tokenStream(String fieldName, Reader reader) {
		Map<String, String> wordRep = new HashMap<String, String>();
		wordRep.put("token", "two tokens");
		Dictionary dictionary = new Acronyms(wordRep);
		
		Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_36, reader);
		TokenFilter dictionaryFilter = new DictionaryFilter(tokenizer, dictionary);

		return dictionaryFilter;
	}
}
