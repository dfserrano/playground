package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TestAnalyzer {

	public static void main(String[] args) {
		TestAnalyzer t = new TestAnalyzer();
		try {
			t.testPorterStemmingAnalyzer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testPorterStemmingAnalyzer() throws IOException {
		List<String> tweets = new ArrayList<String>();

		/*
		 * //English tweets.add(
		 * "Amazing omg story! of @dropbox :) and @apple ? http://t.co/r4oDCwvY"
		 * ); tweets.add("@leg3nd @apple coooool the new iPhone?"); tweets.add(
		 * "shit, shit, shit. IOS5 update ate all my apps, data and media just like @apple said it would. This is going to take some time to rebuild."
		 * ); tweets.add(
		 * "If you've been struggling to get hold of me, I'm back online with a new iPhone - thanks @apple"
		 * ); tweets.add(
		 * "Hey @Apple , pretty much all your products are amazing. You blow minds every time you launch a new gizmo. That said, your hold music is crap."
		 * ); tweets.add(
		 * ". @apple &amp; @AT&amp;T u cannot tell me there isn't at least 1 64GB iPhone 4S in LA or Vegas!! Give me a fucking break!!!!"
		 * );
		 * tweets.add("Amazing story of @dropbox and @apple http://t.co/r4oDCwvY"
		 * ); tweets.add(
		 * "Love @apple downloads. 4 hours and i-pad now wonky! #ripstevejobs #thenonsensepersists #neednewipadguide #fatfuckingchance"
		 * ); tweets.add(
		 * "sail phoenix next stop----&gt; after quimera rock @apple house");
		 * tweets.add(
		 * "Great Tech War of 2012 http://t.co/64jGRoBp @FastCompany profiles @Amazon @Apple @Google @Facebook on @CNBC /ht @philsimon"
		 * ); tweets.add("Very cool #Google browser ''Chrome."); tweets.add(
		 * "I am so done with @Att and @apple 's archi-tech profitering and lack of customer service, so fucking down with both!!!"
		 * );
		 */

		tweets.add("@MeliBotero Ojala y tu orgullo haga la tarea de mate mejor que yo!! Jajjajajjaajjajaj mentiras  bueno!");
		tweets.add("#VivimosEnUnPaísDonde Importa más la belleza que lo que tengas en el cerebro");
		tweets.add("Así los leo ahora. http://t.co/DPnsrQeFNI");
		tweets.add("\"Yo estaba empeñado en no ver lo que vi, pero a veces la vida es más compleja de lo que parece\" Jorge Drexler FELIZ NOCHE");
		tweets.add("Miedo :s");
		tweets.add("Toda mujer tiene algo hermoso…como una amiga. #NosotrosTambiénPodemos");
		tweets.add("Shopping con #MiModeloFavorita, Alistandose para recibir el invierno.");
		tweets.add("RT @esjuanfe: ¿Cómo así? ¿Yo stalkeando? ¡Que le haya dado Fav a un tweet tuyo del 4 de febrero no quiere decir NADA!");
		tweets.add("RT @drunksicons: F(x) banner 6/6 | Da creditos si usas o guardas | drunksicons © | femy. http://t.co/KOcF5qdyqG");
		tweets.add("@MAMW18 jajajaja ya como estas cumpliendo años ya u,u jaja loveuu mono<3");
		tweets.add("@rkmykeny van a estar en #colombia?");

		Analyzer analyzer = new TweetAnalyzer();

		for (String text : tweets) {
			Reader reader = new StringReader(text);
			TokenStream ts = analyzer.tokenStream(null, reader);
			CharTermAttribute charTermAttribute = ts
					.addAttribute(CharTermAttribute.class);

			System.out.println(text);
			while (ts.incrementToken()) {
				String term = charTermAttribute.toString();

				System.out.println("--> " + term + " ");
			}
			System.out.println("\n");
		}

		analyzer.close();
	}
}
