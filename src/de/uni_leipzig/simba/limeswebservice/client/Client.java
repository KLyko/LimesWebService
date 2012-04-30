package de.uni_leipzig.simba.limeswebservice.client;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.axis2.AxisFault;

import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub;
import de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.GetMapping;
import de.uni_leipzig.simba.limeswebservice.util.JsonParser;

public class Client {

	private HashMap<String,Object> source;
	private HashMap<String,Object> target;
	private HashMap<String,Object> metric;
	public Client (){
		source =new HashMap<String,Object>();
		target =new HashMap<String,Object>();
		metric =new HashMap<String,Object>();
	}
	
	public void setInput(String endpoint, String graph, String var ,String clasz,boolean isTarget){
		if (!isTarget){
			source.put("endpoint", endpoint);
			source.put("graph",graph);
			source.put("var", var);
			if (clasz!=null){
				source.put("class", clasz);
			}
		}else{
			target.put("endpoint", endpoint);
			target.put("graph",graph);
			target.put("var", var);
			if (clasz!=null){
				target.put("class", clasz);
			}
		}
	}
	
	public void setProperties (HashMap<String, String> prefixes, HashMap<String, String> properties,
			boolean isTarget){
		if (!isTarget){
			source.put("prefixes", prefixes);
			source.put("properties",properties);
		}else{
			target.put("prefixes", prefixes);
			target.put("properties",properties);
		}
	}
	
	public void setMetric (String metricString){
		metric.put("metric", metricString);
	}
	
	public void setThreshholds(float accT,float revT){
		metric.put("accthreshold", accT);
		metric.put("verthreshold", revT);
	}
	
	public void send(){
		try {
			LimesServiceImplStub stub = new LimesServiceImplStub ();
			GetMapping mapping = new GetMapping();
			mapping.setSourceString(JsonParser.parseJavaToJSON(source));
			mapping.setTargetString(JsonParser.parseJavaToJSON(target));
			mapping.setMetricString(JsonParser.parseJavaToJSON(metric));
			stub.getMapping(mapping);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Object> getSource() {
		return source;
	}

	public void setSource(HashMap<String, Object> source) {
		this.source = source;
	}

	public static void main (String[] args){
		Client client = new Client();
		client.setInput("blug", "dfg", "gfg", "gfg", false);
		client.send();
	}
	
}
