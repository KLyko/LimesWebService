package de.uni_leipzig.simba.limeswebservice.server;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UserManagerTest {
	@Test
	public void testGenerateValidFileName() {
		String test = "http://dbpedia.org/in-sparql";
		assertTrue(UserManager.generateValidFileName(test).equals("dbpedia.org"));		
	}
}
