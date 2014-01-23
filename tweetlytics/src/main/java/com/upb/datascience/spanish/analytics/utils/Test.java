package com.upb.datascience.spanish.analytics.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Test {

	public static void main(String[] args) {
		try {
			InputStream fstream = ClassLoader
					.getSystemResourceAsStream("datasets/positive/sentiment140-train.csv");

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			FileWriter outstream = new FileWriter("sentiment140-train-part.csv");
			BufferedWriter out = new BufferedWriter(outstream);

			Random random = new Random(System.currentTimeMillis());
			String strLine;
			int numTweets = 0;
			String target = "positive";

			// Read File Line By Line (each line a word)
			while ((strLine = br.readLine()) != null) {
				/*
				 * String[] tweetPieces = strLine.split("\",\""); String
				 * polarity = tweetPieces[0].substring(1); String text =
				 * tweetPieces[5].substring(0, tweetPieces[5].length() - 1);
				 */
				String[] tweetPieces = strLine.split("\t");

				if (tweetPieces.length == 2) {
					String polarity = tweetPieces[1];
					String text = tweetPieces[0];

					/*
					 * switch (Integer.parseInt(polarity)) { case 0: polarity =
					 * "negative"; break; case 2: polarity = "neutral"; break;
					 * case 4: polarity = "positive"; break; default: continue;
					 * }
					 */

					if (random.nextInt(2000) < 12) {
						if (polarity.equals(target)
								&& !text.equals("Not Available")) {
							numTweets++;
							out.write(text + "\t" + polarity + "\n");
						}
					}
				}
			}
			System.out.println(numTweets);

			fstream.close();
			br.close();
			in.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
