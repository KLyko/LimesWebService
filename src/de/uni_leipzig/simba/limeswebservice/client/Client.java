package de.uni_leipzig.simba.limeswebservice.client;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.limeswebservice.client.view.LimesJFrame;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplCallbackHandler;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.ContinueSession;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.ContinueSessionResponse;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.FetchMetricMap;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.FetchMetricMapResponse;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.FetchSourceData;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.FetchSourceDataResponse;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.FetchTargetData;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.FetchTargetDataResponse;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.GetMapping;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.GetMetricAdvice;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.GetTrainingData;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.GetTrainingDataResponse;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.LearnMetric;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.LearnMetricResponse;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.Polling;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.SetMetricSpec;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.SetSpecification;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.StartSession;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.StartSessionResponse;


import de.uni_leipzig.simba.limeswebservice.util.JsonParser;

public class Client {

	public static final String GET_SPEC_SOURCE ="getSpecSource";
	public static final String GET_SPEC_TARGET ="getSpecTarget";
	public static final String GET_METRIC_ADVICE ="getMetricAdvice";
	public static final String GET_METRIC ="getMetric";
	private static final Logger log = Logger.getLogger(Client.class);
	private PropertyChangeSupport change;
	private HashMap<String,Object> source;
	private HashMap<String,Object> target;
	private HashMap<String,Object> metric;
	private String emailAddress;
	private int sessionId;
	private LimesJFrame frame = null;
	org.slf4j.Logger logger = LoggerFactory.getLogger(Client.class);
	
	public Client (){
		change = new PropertyChangeSupport(this);
		source =new HashMap<String,Object>();
		target =new HashMap<String,Object>();
		metric =new HashMap<String,Object>();
	}
	
	/**
	 * Constructor to interact with client frame.
	 * @param frame
	 */
	public Client (LimesJFrame frame) {
		this();
		this.frame = frame;
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
	
	
	public void setProperties (HashMap<String, String> properties,
			boolean isTarget){
		if (!isTarget){			
			HashMap<String, String> prefixes = new HashMap<String,String>();
			prefixes.put("rdf", PrefixHelper.getURI("rdf"));
			if (properties != null) {
				for(String prop : properties.keySet()) {
					String pref  = PrefixHelper.getBase(prop);
					prefixes.put(pref, PrefixHelper.getURI(pref));
				}			
				source.put("properties",properties);
			}
			source.put("prefixes", prefixes);
		}else{
			HashMap<String, String> prefixes = new HashMap<String,String>();
			prefixes.put("rdf", PrefixHelper.getURI("rdf"));
			if (properties != null) {
				for(String prop : properties.keySet()) {
					String pref  = PrefixHelper.getBase(prop);
					prefixes.put(pref, PrefixHelper.getURI(pref));
					System.out.println("Adding prefix: "+pref+" - "+ PrefixHelper.getURI(pref));
				}			
				target.put("properties",properties);
			}
			target.put("prefixes", prefixes);
		}
	}
	
	public void setMetric (String metricString){
		if (metricString != null)
		metric.put("metric", metricString);
	}
	
	public void setThreshholds(Double double1,Double double2){
		metric.put("accthreshold", double1);
		metric.put("verthreshold", double2);
	}
	
	
	
	public void sendAll(){
		try {
			
			LimesServiceImplStub stub = new LimesServiceImplStub ();
			GetMapping mapping = new GetMapping();
			
			mapping.setSessionID(sessionId);
			stub.getMapping(mapping);
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMetricSpec(){
		try {
			LimesServiceImplStub session= new LimesServiceImplStub();
			SetMetricSpec spec = new SetMetricSpec();
			spec.setMetricMap(JsonParser.parseJavaToJSON(metric));
			spec.setSessionId(sessionId);
			session.setMetricSpec(spec);
//			System.out.println("start session SessionID="+sessionId);
			logger.info("sending metric spec for session "+sessionId);
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
			LimesServiceImplStub session= new LimesServiceImplStub();
			SetSpecification spec = new SetSpecification();
			spec.setSessionId(sessionId);
			spec.setSource(JsonParser.parseJavaToJSON(source));
			spec.setTarget(JsonParser.parseJavaToJSON(target));
			session.setSpecification(spec);
			logger.info("Sending source&target specification SessionID="+sessionId);
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
	
	
	
	public void getMetricAdvice () throws RemoteException{
		final LimesServiceImplStub limesService= new LimesServiceImplStub();
//		final Polling pol = new Polling();
//		final LimesServiceImplCallbackHandler callback2 = new LimesServiceImplCallbackHandler(){
//			public void receiveResultpolling(
//                    de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.PollingResponse result
//                        ){
////				logger.info("receiveResultpolling");
////				System.out.println("receiveResultpolling");
//           }
//           public void receiveErrorpolling(java.lang.Exception e) {
//        	   log.error(e.getMessage());
//           }
//		};
//		final TimerTask task= new TimerTask(){
//			
//			@Override
//			public void run() {
//				
//				try {
//					limesService.startpolling(pol, callback2);
//				} catch (RemoteException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//			
//		};
//		final Timer t = new Timer ();
//			
		
		LimesServiceImplCallbackHandler callback = new LimesServiceImplCallbackHandler(){
			public void receiveResultgetMetricAdvice(
                    de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.GetMetricAdviceResponse result
                        ){
//				t.cancel();
//				task.cancel();
				logger.info("receiveResultgetMetricAdvice");
//				try{
//					limesService._getServiceClient().getOptions().setUseSeparateListener(false);
//					limesService._getServiceClient().getServiceContext().flush();
//					limesService._getServiceClient().cleanup();
//					
//				}catch (AxisFault af){
//					af.printStackTrace();
//				}
				change.firePropertyChange(GET_METRIC_ADVICE, null, result.get_return());
				
           }
           public void receiveErrorgetMetricAdvice(java.lang.Exception e) {
        	   log.error(e.getMessage());
           }
		};
		GetMetricAdvice req= new GetMetricAdvice(); 
		req.setSessionId(sessionId);
		
		
		System.out.println("start get metric advice using SessionID="+sessionId);
		limesService.startgetMetricAdvice(req,callback);
//		t.schedule(task, 10, 5000);
	
		
	}
	
	public void startSession (String mail){
		try {
			LimesServiceImplStub session= new LimesServiceImplStub();
			
			StartSession startSes = new StartSession();
			
			startSes.setEmailAddress(mail);
			StartSessionResponse res = session.startSession(startSes);
			this.sessionId = res.get_return();
			if(frame != null) {
				// write session id to field
				frame.setSessionId(sessionId);
			}
			System.out.println("start session SessionID="+sessionId);
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
			LimesServiceImplStub session= new LimesServiceImplStub();
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
	
	public boolean learnMetric(Mapping m) {
		try {
			this.sendSpecification();
			this.sendMetricSpec();
			LimesServiceImplStub stub = new LimesServiceImplStub ();
			LearnMetric learnMetric = new LearnMetric();
			learnMetric.setSessionID(sessionId);
	
			learnMetric.setTrainingData(JsonParser.parseMappingToJSON(m));
			LearnMetricResponse lp = stub.learnMetric(learnMetric);
			System.out.println("Got Response from learning method..."+lp.get_return());
			return lp.get_return();

		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public Mapping getTrainingData() {
		try {
//			this.sendSpecification();
//			this.sendMetricSpec();
			LimesServiceImplStub stub = new LimesServiceImplStub ();
			GetTrainingData getTrainData = new GetTrainingData();
			getTrainData.setSessionID(sessionId);		
			GetTrainingDataResponse resp = stub.getTrainingData(getTrainData);
			return JsonParser.parseMappingFromJSONSerialization(resp.get_return());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println("Something went wrong getting training data...");
		return new Mapping();
	}
	
	/**
	 * Get learned metric from Webservice.
	 */
	public void getLearnedMetric() {
		FetchMetricMap metricData = new FetchMetricMap();
		metricData.setSessionId(this.sessionId);
		LimesServiceImplStub session;
		try {
			session = new LimesServiceImplStub();	
		FetchMetricMapResponse resMetric = session.fetchMetricMap(metricData);
		change.firePropertyChange(GET_METRIC, null, 
				JsonParser.parseJSONToJava(resMetric.get_return()));
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
