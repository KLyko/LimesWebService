package de.uni_leipzig.simba.limeswebservice.util;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		jsonO.putAll(map);
		return jsonO.toJSONString();
	}
}
