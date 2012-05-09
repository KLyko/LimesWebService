package de.uni_leipzig.simba.limeswebservice.server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

import de.konrad.commons.sparql.PrefixHelper;
import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.filter.Filter;
import de.uni_leipzig.simba.filter.LinearFilter;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.mapper.SetConstraintsMapper;
import de.uni_leipzig.simba.mapper.SetConstraintsMapperFactory;
import de.uni_leipzig.simba.mapper.SimpleSetConstraintsMapper;

public class LimesUser {

	public static final String MAPPING_READY ="mappingReady";
	private int id ;
	private String filePath;
	private String mailAddress;
	private PropertyChangeSupport change;
	private Mapping result;
	private HashMap<String,Object> sourceMap;
	private HashMap<String,Object> targetMap;
	private HashMap<String,Object> metricMap;

	
	
	


	public  LimesUser (int id,String mailAddress){
		change = new PropertyChangeSupport(this);
		this.id = id;
		this.setMailAddress(mailAddress);
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
	
	
	public void calculateMapping (KBInfo sourceInfo, KBInfo targetInfo,
			String metric,Double accThreshold,Double revThreshold){
		System.out.println("begin calculation");
		if(sourceInfo.prefixes == null)
			sourceInfo.prefixes = new HashMap<String, String>();
		if(targetInfo.prefixes == null)
			targetInfo.prefixes = new HashMap<String, String>();
		sourceInfo.prefixes.put("rdf", PrefixHelper.getURI("rdf"));
		sourceInfo.prefixes.put("dbp", PrefixHelper.getURI("dbp"));
		sourceInfo.prefixes.put("rdfs", PrefixHelper.getURI("rdfs"));
		targetInfo.prefixes.put("rdf", PrefixHelper.getURI("rdf"));
		targetInfo.prefixes.put("dbp", PrefixHelper.getURI("dbp"));
		targetInfo.prefixes.put("rdfs", PrefixHelper.getURI("rdfs"));
		
		HybridCache sC = HybridCache.getData(sourceInfo);
		HybridCache tC = HybridCache.getData(targetInfo);
		SetConstraintsMapper sCM= SetConstraintsMapperFactory.getMapper("simple", sourceInfo, sourceInfo, sC, tC, new LinearFilter(), 2);
		result = sCM.getLinks(metric, accThreshold);
		
		change.firePropertyChange(MAPPING_READY, null, id);
	}
	
	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}


	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}


	public void addPropertyChangeListener(PropertyChangeListener listener){
		change.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		change.removePropertyChangeListener(listener);
	}


	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	public Mapping getResult() {
		return result;
	}


	public void setResult(Mapping result) {
		this.result = result;
	}

	public HashMap<String, Object> getSourceMap() {
		return sourceMap;
	}

	public HashMap<String, Object> getTargetMap() {
		return targetMap;	
	}
	public HashMap<String, Object> getMetricMap() {
		return metricMap;
	}

	public void setTargetMap(HashMap<String, Object> targetMap) {
		this.targetMap = targetMap;
	}

	public void setSourceMap(HashMap<String, Object> sourceMap) {
		this.sourceMap = sourceMap;
	}

	

	public void setMetricMap(HashMap<String, Object> metricMap) {
		this.metricMap = metricMap;
	}
}
