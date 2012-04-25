package de.uni_leipzig.simba.limeswebservice.service;

import java.rmi.Remote;
import java.util.HashMap;

public interface LimesService extends Remote{
	/**
	 * 
	 * @param source HashMap <table>
	 * 	<tr><td>endpoint</td><td>String</td></tr>
	 *  <tr><td>graph OPTIONAL</td><td>String graph URI</td></tr>
	 *  <tr><td>prefix</td><td>HashMap "<"String, String">" prefix - URI</td></tr>
	 *  <tr><td>properties</td><td>HashMap "<"String, String">" property name - preprocessing</td></tr>
	 *  <tr><td>var</td><td>String</td></tr>
	 *  <tr><td>class OPTIONAL</td><td>String class URI</td></tr>
	 *  <tr><td>id</td><td>String</td></tr>
	 *  </table>
	 * @param target HashMap <table>
	 * 	<tr><td>endpoint</td><td>String</td></tr>
	 *  <tr><td>graph OPTIONAL</td><td>String graph URI</td></tr>
	 *  <tr><td>prefix</td><td>HashMap "<"String, String">" prefix - URI</td></tr>
	 *  <tr><td>properties</td><td>HashMap "<"String, String">" property name - preprocessing</td></tr>
	 *  <tr><td>var</td><td>String</td></tr>
	 *  <tr><td>class OPTIONAL</td><td>String class URI</td></tr>
	 *  <tr><td>id</td><td>String</td></tr>
	 *  </table>
	 * @param metrice HashMap <table>
	 *  <tr>metric<td></td><td>String</td></tr>
	 *  <tr>accthreshold<td></td><td>Double</td></tr>
	 *  <tr>verthreshold<td></td><td>Double</td></tr>
	 *  </table>
	 */
	public void getMapping(HashMap<String, Object> source, HashMap<String, Object> target, HashMap<String, Object> metric);
}
