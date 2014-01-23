package com.upb.datascience.spanish.analytics.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Keyboard {

	BufferedReader in;

	public Keyboard() {
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	public int readInt() throws NumberFormatException {
		return Integer.parseInt(this.readLine());
	}

	public double readDouble() throws NumberFormatException {
		return Double.parseDouble(this.readLine());
	}

	public float readFloat() throws NumberFormatException {
		return Float.parseFloat(this.readLine());
	}

	public String readString() {
		return this.readLine();
	}

	private String readLine() {
		try {
			return in.readLine();
		} catch (IOException e) {
			return null;
		}
	}
}
