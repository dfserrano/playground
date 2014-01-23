package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LengthFilter;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.es.SpanishLightStemFilter;
import org.apache.lucene.util.Version;

import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Acronyms;
import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Emoticons;
import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Negations;
import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Stopwords;

public class TweetAnalyzer extends Analyzer {

	public TokenStream tokenStream(String fieldName, Reader reader) {
		String urlRegex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		String urlReplacement = "TAGURL";
		String targetRegex = "@[a-zA-Z0-9_]{1,15}(:){0,1}";
		String targetReplacement = "TAGTARGET";
		String nonWordRegex = "\\W";
		String nonWordReplacement = "";
		String numberRegex = "^[0-9]+$";
		String numberReplacement = "TAGNUMERIC";
		String laughterRegex = "(a|e|i|o|u){0,}((j|h){1,}(a|e|i|o|u){1,}){2,}(j|h){0,}";
		String laughterReplacement = "risa";

		Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_36, reader);
		TokenFilter lowerCaseFilter = new LowerCaseFilter(Version.LUCENE_36,
				tokenizer);
		TokenFilter stopFilter = new StopFilter(Version.LUCENE_36,
				lowerCaseFilter,
				new Stopwords("dictionaries/stopwords-es.txt").getStopwords());
		TokenFilter urlFilter = new RegexFilter(stopFilter, urlRegex,
				urlReplacement, true);
		TokenFilter specialPunctuationFilter = new SpecialPunctuationFilter(
				urlFilter);
		TokenFilter targetFilter = new RegexFilter(specialPunctuationFilter,
				targetRegex, targetReplacement, true);
		TokenFilter laughterFilter = new RegexFilter(targetFilter,
				laughterRegex, laughterReplacement, true);
		TokenFilter diacriticFilter = new DiacriticFilter(laughterFilter);

		// Dictionaries
		TokenFilter emoticonsFilter = new DictionaryFilter(diacriticFilter,
				new Emoticons());
		TokenFilter acronymsFilter = new DictionaryFilter(emoticonsFilter,
				new Acronyms());
		TokenFilter negationsFilter = new DictionaryFilter(acronymsFilter,
				new Negations());

		TokenFilter numberFilter = new RegexFilter(negationsFilter,
				numberRegex, numberReplacement, true);
		TokenFilter specialCharsFilter = new RegexFilter(numberFilter,
				nonWordRegex, nonWordReplacement);
		TokenFilter emptyFilter = new EmptyFilter(specialCharsFilter);
		TokenFilter lengthFilter = new LengthFilter(true, emptyFilter, 3, 25);
		TokenFilter repeatedLetterFilter = new RepeatedLetterFilter(
				lengthFilter);

		// TokenFilter stemFilter = new PorterStemFilter(repeatedLetterFilter);

		TokenFilter stemFilter = new SpanishLightStemFilter(
				repeatedLetterFilter);

		return stemFilter;
	}
}
