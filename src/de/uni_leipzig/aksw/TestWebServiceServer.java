package de.uni_leipzig.aksw;

import javax.xml.ws.Endpoint;

public class TestWebServiceServer {
	public static void main(String args[]) {
		TestWebService server = new TestWebService();
		Endpoint endpoint = Endpoint.publish("http://localhost:8080/calculator", server);
	}
}
