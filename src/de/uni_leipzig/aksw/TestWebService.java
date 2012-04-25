package de.uni_leipzig.aksw;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
@WebService
@SOAPBinding(style=Style.RPC)
public class TestWebService {
	
	
	/**
	 * value1 + value2
	 * @param value1
	 * @param value2
	 * @return value1 + value2
	 */
	public long add(int value1, int value2) {
		return value1+value2;
	}
}
