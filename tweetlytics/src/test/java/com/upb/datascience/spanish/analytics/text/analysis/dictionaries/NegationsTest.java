package com.upb.datascience.spanish.analytics.text.analysis.dictionaries;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Negations;

public class NegationsTest extends TestCase {

	private static Negations negations = new Negations();

	@Test
	public void testContains() {
		// Typical negation
		Assert.assertTrue(negations.contains("don't"));

		// Non negation
		Assert.assertFalse(negations.contains("york"));
	}

	@Test
	public void testGetValue() {
		// Typical negation
		Assert.assertEquals(negations.getValue("don't"), "NOT");
		
		// Non negation
		Assert.assertNull(negations.getValue("york"));
	}

}
