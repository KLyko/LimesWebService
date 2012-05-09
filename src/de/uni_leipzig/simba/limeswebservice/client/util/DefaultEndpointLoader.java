package de.uni_leipzig.simba.limeswebservice.client.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.io.KBInfo;

public class DefaultEndpointLoader
{
	public static final String DBPAUSTRONAUTSSOURCE = "DBP-Austronaut-source";
	public static final String DBPAUSTRONAUTSTARGET = "DBP-Austronaut-target";
	/**
	 * 
	 * @param endpoint
	 * @param graph
	 * @param pageSize
	 * @param id
	 * @param var
	 * @param className
	 * @param properties
	 * @return
	 */
	static public KBInfo createKBInfo(String endpoint, String graph, int pageSize, String id
			, String var, String className, List<String> properties)
	{		
		KBInfo kb = new KBInfo();
		kb.prefixes.put("rdf", PrefixHelper.getURI("rdf"));
		kb.endpoint=endpoint;
		kb.graph=graph;
		kb.pageSize=pageSize;
		kb.id=id;
		kb.var = var;
		if(className != null && className.length()>0)
			kb.restrictions.add(className);
		for(String s : properties) {
			if(s.startsWith("http:")) {
				kb.prefixes.put(PrefixHelper.generatePrefix(s)[0], PrefixHelper.generatePrefix(s)[1]);
			} else {
				kb.prefixes.put(PrefixHelper.getBase(s), PrefixHelper.getURI(PrefixHelper.getBase(s)));
			}
			if(!s.contains("AS"))
				kb.properties.add(s+ " AS lowercase");
			else
				kb.properties.add(s);
		}			
		return kb;
	}
	
	/**
	 * Returns named default endpoints.
	 * @return
	 */
	public static HashMap<String, KBInfo> getDefaultEndpoints() {
		HashMap<String, KBInfo> defaults = new HashMap<String,KBInfo>();
		LinkedList<String> props = new LinkedList<String>();
		props.add("rdfs:label");
		props.add("dbp:name");
		props.add("dbp:nationality");		
		defaults.put(DBPAUSTRONAUTSSOURCE, createKBInfo("http://dbpedia.org/sparql",
				"http://dbpedia.org",10000,"dbpedia",
				"?x", "http://dbpedia.org/ontology/Astronaut", props));	
		defaults.put(DBPAUSTRONAUTSTARGET, createKBInfo("http://dbpedia.org/sparql",
				"http://dbpedia.org",10000,"dbpedia",
				"?y", "http://dbpedia.org/ontology/Astronaut", props));	
		
		return defaults;
	}
	
	public static String generateIntialMetric(String key1, String key2) {
		KBInfo i1 = getDefaultEndpoints().get(key1);
		KBInfo i2 = getDefaultEndpoints().get(key2);
		String metric = "levensthein(";
		metric += i1.var.replaceAll("\\?", "")+"."+i1.properties.get(0).split(" ")[0];
		metric += ",";
		metric += i2.var.replaceAll("\\?", "")+"."+i2.properties.get(0).split(" ")[0];
		metric += ")";
		return metric;				
	}
}