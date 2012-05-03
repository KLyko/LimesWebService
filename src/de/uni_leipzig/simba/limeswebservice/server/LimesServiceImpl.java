package de.uni_leipzig.simba.limeswebservice.server;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.parser.ParseException;

import de.konrad.commons.sparql.PrefixHelper;
import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.limeswebservice.service.LimesService;
import de.uni_leipzig.simba.limeswebservice.util.JsonParser;

public class LimesServiceImpl {

	public void getMapping(String sourceString, String targetString,
			String metricString,String mailAddress){
		String hash = mailAddress+System.currentTimeMillis();
		int  id = hash.hashCode();
		LimesExecutor le =new LimesExecutor (id,mailAddress);
		le.addPropertyChangeListener(UserManager.getInstance());
		UserManager.getInstance().addUser(id, le);
		
		HashMap<String, Object> source;
		try {
			source = JsonParser.parseJSONToJava(sourceString);
		
		HashMap<String,Object> target= JsonParser.parseJSONToJava(targetString);
		HashMap<String,Object> metric= JsonParser.parseJSONToJava(metricString);
		
		KBInfo sourceInfo = createKBInfo(source);
		KBInfo targetInfo = createKBInfo(target);
		// get metric
		
		String metricExpr = (String) metric.get("metric");
		Double accThreshold = (Double) metric.get("accthreshold");
		Double verThreshold = (Double) metric.get("verthreshold");
		le.calculateMapping(sourceInfo, targetInfo, metricExpr, accThreshold, verThreshold);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private KBInfo createKBInfo(HashMap<String, Object> param) {
		KBInfo info = new KBInfo();
		info.endpoint = (String) param.get("endpoint");
		for (String key: param.keySet()){
			System.out.println(key+"\t"+param.get(key));
			
		}
		info.graph = (String) param.get("graph");
		info.var = (String) param.get("var");
		System.out.println(info.endpoint);
		info.restrictions = new ArrayList<String>();
		if(param.containsKey("class")) {
			String classRestrString = info.var+" rdf:type "+SPARQLHelper.wrapIfNecessary((String)param.get("class"));
			info.restrictions.add(classRestrString);
		}
		info.prefixes = (HashMap<String, String>) param.get("prefix");
		
		info.functions = (HashMap<String, String>) param.get("properties");
		for(String prop : info.functions.keySet()) {
			info.properties.add(prop);
			
		}
		info.type = "SPARQL";
		info.id = (String) param.get("id");
		return info;
	}
}

