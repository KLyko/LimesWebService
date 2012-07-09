package de.uni_leipzig.simba.limeswebservice.server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.filter.LinearFilter;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.mapper.SetConstraintsMapper;
import de.uni_leipzig.simba.mapper.SetConstraintsMapperFactory;

public class LimesUser implements Comparable {

	public static final String MAPPING_READY ="mappingReady";
	private int id ;
	private String filePath;
	private String mailAddress;
	private PropertyChangeSupport change;
	private Mapping result;
	private HashMap<String,Object> sourceMap;
	private HashMap<String,Object> targetMap;
	private HashMap<String,Object> metricMap;
	private long noUsageTime;
	
	
	


	public  LimesUser (int id,String mailAddress){
		
		change = new PropertyChangeSupport(this);
		noUsageTime =0;
		this.id = id;
		this.setMailAddress(mailAddress);
	}
	
	public void calculateMapping (KBInfo sourceInfo, KBInfo targetInfo,
			String metric,Double accThreshold,Double revThreshold){
		System.out.println("begin calculation");
		if(sourceInfo.prefixes == null)
			sourceInfo.prefixes = new HashMap<String, String>();
		if(targetInfo.prefixes == null)
			targetInfo.prefixes = new HashMap<String, String>();
//		sourceInfo.prefixes.put("rdf", PrefixHelper.getURI("rdf"));
//		sourceInfo.prefixes.put("dbp", PrefixHelper.getURI("dbp"));
//		sourceInfo.prefixes.put("rdfs", PrefixHelper.getURI("rdfs"));
//		targetInfo.prefixes.put("rdf", PrefixHelper.getURI("rdf"));
//		targetInfo.prefixes.put("dbp", PrefixHelper.getURI("dbp"));
//		targetInfo.prefixes.put("rdfs", PrefixHelper.getURI("rdfs"));
//		
		HybridCache sC = HybridCache.getData(sourceInfo);
		HybridCache tC = HybridCache.getData(targetInfo);
		SetConstraintsMapper sCM= SetConstraintsMapperFactory.getMapper("simple", sourceInfo, sourceInfo, sC, tC, new LinearFilter(), 2);
		
		result = sCM.getLinks(metric, accThreshold);
		
		change.firePropertyChange(MAPPING_READY, null, id);
		this.removePropertyChangeListener(UserManager.getInstance());
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

	public long getNoUsageTime() {
		return noUsageTime;
	}

	public void setNoUsageTime(long noUsageTime) {
		this.noUsageTime = noUsageTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int compareTo(Object o) {
		LimesUser lu1 = this;
		LimesUser lu2 = (LimesUser) o ;
		return ((Long)lu1.noUsageTime).compareTo((Long)lu2.noUsageTime);
		
	}
}
