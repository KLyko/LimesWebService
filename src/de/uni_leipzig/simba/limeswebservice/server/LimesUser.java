package de.uni_leipzig.simba.limeswebservice.server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.jgap.InvalidConfigurationException;

import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.filter.LinearFilter;
import de.uni_leipzig.simba.genetics.core.Metric;
import de.uni_leipzig.simba.genetics.learner.GeneticActiveLearner;
import de.uni_leipzig.simba.genetics.util.PropertyMapping;
import de.uni_leipzig.simba.io.ConfigReader;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.limeswebservice.util.JsonParser;
import de.uni_leipzig.simba.mapper.SetConstraintsMapper;
import de.uni_leipzig.simba.mapper.SetConstraintsMapperFactory;

public class LimesUser implements Comparable {

	public static final String MAPPING_READY ="mappingReady";
	public static final String ERROR_COMPUTING_MAPPING = "errorComputingMapping";
	public static final String MAPPING_EMPTY = "mappingEmpty";
	private int id ;
	private String filePath;
	private String mailAddress;
	private PropertyChangeSupport change;
	private Mapping result;
	private HashMap<String,Object> sourceMap;
	private HashMap<String,Object> targetMap;
	private HashMap<String,Object> metricMap;
	private long noUsageTime;
	
	
	private GeneticActiveLearner learner = null;
	private Mapping toEvaluate = null;
	private Metric learnedMetric = null;
//	private KBInfo source, target;

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
	
		try {
			HybridCache sC = HybridCache.getData(new File(System.getProperty("user.home")), sourceInfo);
			HybridCache tC = HybridCache.getData(new File(System.getProperty("user.home")), targetInfo);
//			HybridCache sC = HybridCache.getData( sourceInfo);
//			HybridCache tC = HybridCache.getData( targetInfo);
			SetConstraintsMapper sCM= SetConstraintsMapperFactory.getMapper("simple", sourceInfo, sourceInfo, sC, tC, new LinearFilter(), 2);
			
			// remember settings to init learner
			result = sCM.getLinks(metric, accThreshold);
			
			if(result != null && result.size() > 0) {
				change.firePropertyChange(MAPPING_READY, null, id);
			}
			else {
				if(result == null) {
					change.firePropertyChange(ERROR_COMPUTING_MAPPING, null, id);
					System.out.println("Calculated Mapping was null");
				} else if (result.size() == 0) {
					System.out.println("Calculated Mapping was empty");
					change.firePropertyChange(MAPPING_EMPTY, null, id);
				}
			}
		} catch(Exception e) {
			change.firePropertyChange(ERROR_COMPUTING_MAPPING, null, id);
		}
		finally {
			this.removePropertyChangeListener(UserManager.getInstance());
		}
		
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

	public Metric getLearnedMetric() {
		return learnedMetric;
	}

	private void setLearnedMetric(Metric learnedMetric) {
		metricMap.put("metric", learnedMetric.getExpression());
		metricMap.put("accthreshold", learnedMetric.getThreshold());
//		metricMap.put("verthreshold", double2);
		this.learnedMetric = learnedMetric;
	}

	public Mapping getToEvaluate() {
		return toEvaluate;
	}

	public void setToEvaluate(Mapping toEvaluate) {
		this.toEvaluate = toEvaluate;
	}
	
	/**
	 * Active Learning approach.
	 * As we have to serialize Hashmaps into JSON Strings please see the JSONParser class for
	 * details how to reconstruct it.
	 * @param trainingData
	 * @return
	 */
	public boolean learn(String trainingData) {
		boolean setMetric = true;
		if(learner == null) {
			setMetric = false;
			System.out.println("Initializing learner...");
			learner = new GeneticActiveLearner();
			try {
				KBInfo sourceInfo = createKBInfo(sourceMap);
				KBInfo targetInfo = createKBInfo(targetMap);
				// get metric
				learnedMetric = new Metric((String) metricMap.get("metric"), (Double) metricMap.get("accthreshold"));
				learner.init(sourceInfo, targetInfo, getParams());
				System.out.println("Learner was successfully initialized");
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
				return false;
			}
		}
		Mapping learnMap = JsonParser.parseMappingFromJSONSerialization(trainingData);

		System.out.println("Learning begins. With training data: "+learnMap.toString());
		toEvaluate = learner.learn(learnMap);
		System.out.println("Learning has finished...");
		if(setMetric && learnMap.size()>0) {
			System.out.println("Determining learned metric...");
			try {
				setLearnedMetric(learner.terminate());				
			}catch(Exception e){
				//FIXME error handling
			}
		}
		return true;
	}
	
	
	private HashMap<String, Object> getParams() {
		KBInfo source = createKBInfo(sourceMap);
		KBInfo target = createKBInfo(targetMap);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("populationSize", 20);
		parameters.put("generations", 20);
		parameters.put("mutationRate", 0.6f);
		parameters.put("crossoverRate", 0.6f);
		parameters.put("preserveFittest", true);
		parameters.put("granularity", 2);
		parameters.put("trainingDataSize", 10);
		PropertyMapping propMap = new PropertyMapping();
		for(String sProp : source.properties)
			for(String tProp : target.properties)
				propMap.addStringPropertyMatch(sProp, tProp);
		parameters.put("propertyMapping", propMap);
		ConfigReader cR = new ConfigReader();
		cR.sourceInfo = source;
		cR.targetInfo = target;
		cR.metricExpression = learnedMetric.getExpression();
		cR.acceptanceThreshold = learnedMetric.getThreshold();
		cR.verificationThreshold = (learnedMetric.getThreshold()*0.98d);
		parameters.put("config", cR);
		return parameters;
	}
	
	
	public KBInfo createKBInfo(HashMap<String, Object> param) {
		KBInfo info = new KBInfo();
		info.endpoint = (String) param.get("endpoint");
		
		info.graph = (String) param.get("graph");
		info.var = (String) param.get("var");
		System.out.println(info.endpoint);
		info.restrictions = new ArrayList<String>();
		if(param.containsKey("class")) {
			String classRestrString = info.var+" rdf:type "+SPARQLHelper.wrapIfNecessary((String)param.get("class"));
			info.restrictions.add(classRestrString);
		}
		info.prefixes = (HashMap<String, String>) param.get("prefixes");
		System.out.println("PREFIXES: "+info.prefixes);
		HashMap<String, String> old = (HashMap<String, String>) param.get("properties");
		for(String key : old.keySet()) {
			info.functions.put(key,  new HashMap<String,String>());
			info.functions.get(key).put(key, old.get(key));
		}
		for(String prop : info.functions.keySet()) {
			info.properties.add(prop);
			
		}
		info.type = "SPARQL";
		if(param.get("id") != null)
			info.id = (String) param.get("id");
		else
			info.id = (String) param.get("endpoint");
		return info;
	}

	/**
	 * Method to unset learner.
	 */
	public void cancelLearning() {
		if(learner != null)
			learner = null;
	}
}
