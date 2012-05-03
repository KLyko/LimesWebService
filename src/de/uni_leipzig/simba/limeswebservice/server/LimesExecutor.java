package de.uni_leipzig.simba.limeswebservice.server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.filter.Filter;
import de.uni_leipzig.simba.filter.LinearFilter;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.mapper.SetConstraintsMapper;
import de.uni_leipzig.simba.mapper.SetConstraintsMapperFactory;
import de.uni_leipzig.simba.mapper.SimpleSetConstraintsMapper;

public class LimesExecutor {

	public static final String MAPPING_READY ="mappingReady";
	private int id ;
	private String filePath;
	private String mailAddress;
	private PropertyChangeSupport change;
	private Mapping result;
	
	
	public  LimesExecutor (int id,String mailAddress){
		change = new PropertyChangeSupport(this);
		this.id = id;
		this.setMailAddress(mailAddress);
	}
	
	
	public void calculateMapping (KBInfo sourceInfo, KBInfo targetInfo,
			String metric,Double accThreshold,Double revThreshold){
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
		System.out.println(result);
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
}
