package com.upb.datascience.spanish.analytics.text.analysis;

public class PolarTweet {

	public static int POSITIVE = 3;
	public static int NEUTRAL = 2;
	public static int NEGATIVE = 1;

	private String text;
	private int polarity;

	public PolarTweet(String text, int polarity) {
		this.text = text;
		this.polarity = polarity;
	}

	public String getText() {
		return text;
	}

	public int getPolarity() {
		return polarity;
	}

	public static String getPolarityLabel(int number) {
		switch (number) {
		case 1:
			return "NEGATIVE";
		case 2:
			return "NEUTRAL";
		case 3:
			return "POSITIVE";
		default:
			return "UNKNOWN";
		}
	}
}
