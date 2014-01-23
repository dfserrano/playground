package com.upb.datascience.spanish.analytics.text.analysis.dictionaries;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Acronyms;

public class AcronymsTest extends TestCase {

	private static Acronyms acronyms = new Acronyms();

	@Test
	public void testContains() {
		// Typical acronym
		Assert.assertTrue(acronyms.contains("wtf"));

		// Non acronym
		Assert.assertFalse(acronyms.contains("york"));
	}

	@Test
	public void testGetValue() {
		// Typical acronym
		Assert.assertEquals(acronyms.getValue("wtf"), "what the fuck");
		
		// Non acronym
		Assert.assertNull(acronyms.getValue("york"));
	}

}
