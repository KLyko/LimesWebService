package de.uni_leipzig.simba.limeswebservice.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import de.uni_leipzig.simba.data.Mapping;

public class JsonParser {
	
	public static HashMap<String,Object> parseJSONToJava(String jsonString) throws ParseException{
		JSONParser parser = new JSONParser();
		HashMap<String,Object> map = new HashMap<String,Object>();
		JSONObject object= (JSONObject) parser.parse(jsonString);
		
		for (Object o :object.keySet()){
			if (!(object.get(o) instanceof JSONObject))
				map.put(o.toString(), object.get(o));
			else{
				if (object.get(o)!=null){
					
					map.put(o.toString(), innerMap((JSONObject)object.get(o)));
				}else{
					map.put(o.toString(),new HashMap<String,String>());
				}
			}
		}
		return map;
	}
	
	private static  HashMap<String,String> innerMap (JSONObject obj){
		HashMap<String,String> map = new HashMap<String,String>();
		
		for (Object o :obj.keySet()){
				
				map.put(o.toString(), (obj.get(o)==null)?"":obj.get(o).toString());	
		}
		return map;
	}
	
	public static String parseJavaToJSON(Map<String,Object> map){		
		JSONObject jsonO = new JSONObject ();
		if (map ==null){
			return jsonO.toJSONString();
		}else{
			jsonO.putAll(map);
			return jsonO.toJSONString();
		}
	}
	
	@Test
	public void testParser() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> a1 = new HashMap<String, Object>();
		Map<String, Object> a2 = new HashMap<String, Object>();
		//HashMap<String, Double> a1 = new HashMap<String, Double>();
		a1.put("a", 1d);
		a2.put("b", 0.9d);
		a2.put("c", 0d);
		map.put("A", a1);
		map.put("B", a2);
		
		String mapJSON = parseJavaToJSON(map);
		System.out.println(mapJSON);
		System.out.println("-------------");
		for(Entry<String, Object> e : map.entrySet()) {
			System.out.println(e.getKey() + " => " + e.getValue().toString() );
		}
		
		try {
			HashMap<String, Object> parsedMap = parseJSONToJava(mapJSON);
			boolean test =  true;
			for(Entry<String, Object> e : parsedMap.entrySet()) {
				if(map.containsKey(e.getKey())) {
					Map<String, Object> parsedObject = 	(Map<String, Object>)e.getValue();
					for(Entry subE : parsedObject.entrySet()) {
						Map<String,Object> org = (Map<String,Object>) map.get(e.getKey());
						if(org.containsKey(subE.getKey())) {
							Double d1 = Double.parseDouble(org.get(subE.getKey()).toString());
							Double d2 = Double.parseDouble(parsedObject.get(subE.getKey()).toString());
							if(Math.abs(d1-d2)>0.05) {
								test = false;
								System.out.println("Object values for "+subE.toString()+" at "+e.getKey() + " are not equal!");
							}
						}else {
							test = false;
							System.out.println("Orginal has no value "+subE.toString()+" at "+e.getKey());
						}
					}
				}else {
					test = false;
					System.out.println("Orginal has no key "+e.getKey());
				}
			}
			assertTrue(test);
		}catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			assertTrue(false);
		}
	}
	
	/**
	 * The JSON str is a JSONSerialization of Map<String, Map<String, Double>>. We will construct a LIMES
	 * mapping out of it.
	 * @param JSONmap A nested JSONSerialization of Map<String, Map<String, Double>>.
	 * @return The parsed Mapping.
	 */
	public static Mapping parseMappingFromJSONSerialization(String JSONmap) {
		Mapping m = new Mapping();
		if(JSONmap == null || JSONmap.length() == 0)
			return m;
		try {
			HashMap<String, Object> parsedMap = parseJSONToJava(JSONmap);
			for(Entry<String, Object> e : parsedMap.entrySet()) {
				Map<String, Object> parsedObject = 	(Map<String, Object>)e.getValue();
				for(Entry<String, Object> subE : parsedObject.entrySet()) {
					Double d;
					try {
						d = Double.parseDouble(subE.getValue().toString());
					}catch(NumberFormatException ex) {
						d = 0d;
						System.err.println("NumberFormatException occured trying to parse the value of "+subE.toString()+
								" at "+e.getValue() +" of the JSON String "+JSONmap+". Using 0 instead;");
					}
					m.add(e.getKey(), subE.getKey(), d);
				}
			}
		}catch(Exception e) {
			System.err.println("There occured a "+e.getMessage()+" Exception trying to parse "+JSONmap+".");
		}		
		return m;
	}

	@Test
	public void testParseMappingFromJSONSerialization() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> a1 = new HashMap<String, Object>();
		Map<String, Object> a2 = new HashMap<String, Object>();
		//HashMap<String, Double> a1 = new HashMap<String, Double>();
		a1.put("a", 1d);
		a2.put("b", 0.9d);
		a2.put("c", 0d);
		map.put("A", a1);
		map.put("B", a2);
		
		Mapping testMap = new Mapping();
		testMap.add("A", "a", 1d);
		testMap.add("B", "b", 0.9d);
		testMap.add("B", "c", 0d);
		
		String mapJSON = parseJavaToJSON(map);
		Mapping parsedMap = parseMappingFromJSONSerialization(mapJSON);
		boolean test = true;
		if(parsedMap.size() != 3) {
			test = false;
			System.out.println("Parsed map has unexpected size.");
		}
		for(Entry<String, HashMap<String, Double>> entry : testMap.map.entrySet())
			for(Entry<String, Double> subEntry : testMap.map.get(entry.getKey()).entrySet()) {
				if(!parsedMap.contains(entry.getKey(), subEntry.getKey())) {
					System.out.println("Map doesn't contained "+entry.getKey()+" => "+subEntry.getKey());
					test = false;
				} else {
					// also check doubles
					Double d1 = testMap.getSimilarity(entry.getKey(), subEntry.getKey());
					Double d2 = parsedMap.getSimilarity(entry.getKey(), subEntry.getKey());
					if(Math.abs(d1-d2) >= 0.005) {
						System.out.println("Double mismatch on "+entry.getKey()+" => "+subEntry.getKey()+" expected "+d1+" vs. got "+d2);
						test = false;
					}
				}
			}
		assertTrue(test);
	}
}
