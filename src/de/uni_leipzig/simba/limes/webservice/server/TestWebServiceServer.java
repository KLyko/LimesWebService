package de.uni_leipzig.simba.limes.webservice.server;

import javax.xml.ws.Endpoint;

import de.uni_leipzig.simba.limes.webservice.core.TestWebService;

public class TestWebServiceServer {
	public static void main(String args[]) {
		TestWebService server = new TestWebService();
		Endpoint endpoint = Endpoint.publish("http://localhost:8080/calculator", server);
	}
}
