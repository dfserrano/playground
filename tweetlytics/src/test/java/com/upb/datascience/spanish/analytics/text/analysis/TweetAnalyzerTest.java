package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import com.upb.datascience.spanish.analytics.text.analysis.TweetAnalyzer;

public class TweetAnalyzerTest {

	@Test
	public void test() {
		try {
			List<String> tweets = new ArrayList<String>();
			tweets.add("Amazing omg story! of @dropbox :) and @apple ? http://t.co/r4oDCwvY");
			tweets.add("@leg3nd @apple coooool the new iPhone?");
			tweets.add("If you've been struggling to get hold of me, I'm back online with a new iPhone - thanks @apple");
			tweets.add(". @apple &amp; @AT&amp;T u cannot tell me there isn't at least 1 64GB iPhone 4S in LA or Vegas!! Give me a fucking break!!!!");
			tweets.add("Very cool #Google browser ''Chrome.");

			List<String> tokenizedTweets = new ArrayList<String>();
			tokenizedTweets
					.add("amaz god stori TAGEXCLAMATION TAGTARGET happi TAGTARGET TAGQUESTION TAGURL ");
			tokenizedTweets
					.add("TAGTARGET TAGTARGET coool new iphon TAGQUESTION ");
			tokenizedTweets
					.add("youv struggl hold back onlin new iphon thank TAGTARGET ");
			tokenizedTweets
					.add("TAGTARGET amp atamp you NOT tell NOT TAGNUMERIC 64gb iphon vega TAGEXCLAMATION TAGEXCLAMATION give fuck break TAGEXCLAMATION TAGEXCLAMATION TAGEXCLAMATION TAGEXCLAMATION ");
			tokenizedTweets.add("veri cool googl browser chrome ");

			Analyzer analyzer = new TweetAnalyzer();

			for (int i = 0; i < tweets.size(); i++) {
				Reader reader = new StringReader(tweets.get(i));
				TokenStream ts = analyzer.tokenStream(null, reader);
				CharTermAttribute charTermAttribute = ts
						.addAttribute(CharTermAttribute.class);

				String actual = "";
				while (ts.incrementToken()) {
					String term = charTermAttribute.toString();

					actual += term + " ";
				}
				
				System.out.println(tokenizedTweets.get(i));
				System.out.println(actual);
				Assert.assertEquals(tokenizedTweets.get(i), actual);
			}

			analyzer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}