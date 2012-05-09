package de.uni_leipzig.simba.limeswebservice.client;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Observable;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.GetMapping;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.ContinueSession;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.ContinueSessionResponse;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.FetchMetricMap;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.FetchMetricMapResponse;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.FetchSourceData;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.FetchSourceDataResponse;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.FetchTargetData;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.FetchTargetDataResponse;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.SetSpecification;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.StartSession;
import de.uni_leipzig.simba.limeswebservice.server.SessionServiceImplStub.StartSessionResponse;
import de.uni_leipzig.simba.limeswebservice.util.JsonParser;

public class Client {

	public static final String GET_SPEC_SOURCE ="getSpecSource";
	public static final String GET_SPEC_TARGET ="getSpecTarget";
	public static final String GET_METRIC ="getMetric";
	private static final Logger log = Logger.getLogger(Client.class);
	private PropertyChangeSupport change;
	private HashMap<String,Object> source;
	private HashMap<String,Object> target;
	private HashMap<String,Object> metric;
	private String emailAddress;
	private int sessionId;
	
	
	public Client (){
		change = new PropertyChangeSupport(this);
		source =new HashMap<String,Object>();
		target =new HashMap<String,Object>();
		metric =new HashMap<String,Object>();
	}
	
	public void setInput(String endpoint, String graph, String var ,String clasz,boolean isTarget){
		if (!isTarget){
			if (endpoint!= null)
				source.put("endpoint", endpoint);
			if (graph!= null)
				source.put("graph",graph);
			if (var != null)
				source.put("var", var);
			if (clasz!=null){
				source.put("class", clasz);
			}
		}else{
			if (endpoint != null)
				target.put("endpoint", endpoint);
			if (graph!= null)
				target.put("graph",graph);
			if (var != null)
				target.put("var", var);
			if (clasz!=null){
				target.put("class", clasz);
			}
		}
	}
	
	
	public void setProperties (HashMap<String, String> prefixes, HashMap<String, String> properties,
			boolean isTarget){
		if (!isTarget){
			if (prefixes != null)
				source.put("prefixes", prefixes);
			if (properties != null)
				source.put("properties",properties);
		}else{
			if (prefixes != null)
				target.put("prefixes", prefixes);
			if (properties != null)
				target.put("properties",properties);
		}
	}
	
	public void setMetric (String metricString){
		if (metricString != null)
		metric.put("metric", metricString);
	}
	
	public void setThreshholds(float accT,float revT){
		metric.put("accthreshold", accT);
		metric.put("verthreshold", revT);
	}
	
	
	
	public void sendAll(){
		try {
			
			LimesServiceImplStub stub = new LimesServiceImplStub ();
			GetMapping mapping = new GetMapping();
			mapping.setSourceString(JsonParser.parseJavaToJSON(source));
			mapping.setTargetString(JsonParser.parseJavaToJSON(target));
			mapping.setMetricString(JsonParser.parseJavaToJSON(metric));
			mapping.setMailAddress(emailAddress);
			stub.getMapping(mapping);
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendSpecification(){
		try {
			SessionServiceImplStub session= new SessionServiceImplStub();
			SetSpecification spec = new SetSpecification();
			spec.setSessionId(sessionId);
			spec.setSource(JsonParser.parseJavaToJSON(source));
			spec.setTarget(JsonParser.parseJavaToJSON(target));
			session.setSpecification(spec);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public HashMap<String,String> getPropertyAdvice(){
		return null;
	}
	
	public String getMetricAdvice (){
		return null;
	}
	
	public void startSession (String mail){
		try {
			SessionServiceImplStub session= new SessionServiceImplStub();
			StartSession startSes = new StartSession();
			startSes.setEmailAddress(mail);
			StartSessionResponse res = session.startSession(startSes);
			this.sessionId = res.get_return();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void continueSession (int sessionId){
		try {
			SessionServiceImplStub session= new SessionServiceImplStub();
			ContinueSession cs = new ContinueSession();
			cs.setSessionId(sessionId);
			ContinueSessionResponse res = session.continueSession(cs);
			int id = res.get_return();
			
			if (id!=-1){
				log.info("continue old session...");
				this.sessionId = id;
				FetchSourceData sourceData =new FetchSourceData();
				FetchTargetData targetData =new FetchTargetData();
				FetchMetricMap metricData = new FetchMetricMap();
				sourceData.setSessionId(this.sessionId);
				targetData.setSessionId(this.sessionId);
				metricData.setSessionId(this.sessionId);
				FetchSourceDataResponse resSource =session.fetchSourceData(sourceData);
				change.firePropertyChange(GET_SPEC_SOURCE, null, 
						JsonParser.parseJSONToJava(resSource.get_return()));
				
				FetchTargetDataResponse resTarget =session.fetchTargetData(targetData);
				change.firePropertyChange(GET_SPEC_TARGET, null, 
						JsonParser.parseJSONToJava(resTarget.get_return()));
				
				FetchMetricMapResponse resMetric = session.fetchMetricMap(metricData);
				change.firePropertyChange(GET_METRIC, null, 
						JsonParser.parseJSONToJava(resMetric.get_return()));
			}else{
				log.info("no old session available");
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public HashMap<String, Object> getSource() {
		return source;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setSource(HashMap<String, Object> source) {
		this.source = source;
	}

	public static void main (String[] args){
		Client client = new Client();
		client.setInput("blug", "dfg", "gfg", "gfg", false);
		client.sendAll();
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the sessionId
	 */
	public int getSessionId() {
		return sessionId;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		change.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		change.removePropertyChangeListener(listener);
	}
	
}
