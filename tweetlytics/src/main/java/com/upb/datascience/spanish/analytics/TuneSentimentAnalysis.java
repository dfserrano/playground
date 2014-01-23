package com.upb.datascience.spanish.analytics;

import com.upb.datascience.spanish.analytics.classification.SVMSentimentAnalysis;

public class TuneSentimentAnalysis {

	public static void main(String[] args) {

		String path = "datasets/elections2008.csv";
		SVMSentimentAnalysis analysis = new SVMSentimentAnalysis(path, 100);

		double bestC = 0;
		double bestG = 0;
		double bestAccuracy = 0;

		for (double c = 0.5; c <= 64; c = c + 2) {
			for (double g = 0.0025; g <= 3; g = g + 0.01) {

				double accuracy = analysis.getAccuracyFromCrossValidation(10,
						c, g, true);

				if (accuracy > bestAccuracy) {
					bestC = c;
					bestG = g;

					System.out.println("CURRENT BEST. C=" + bestC + "  Gamma="
							+ bestG + " (" + accuracy + ")");
				}
			}
		}

		System.out.println("CURRENT BEST. C=" + bestC + "  Gamma=" + bestG
				+ " (" + bestAccuracy + ")");
	}
}
