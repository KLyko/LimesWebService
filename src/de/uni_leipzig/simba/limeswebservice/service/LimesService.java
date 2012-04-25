package de.uni_leipzig.simba.limeswebservice.service;

import java.rmi.Remote;
import java.util.HashMap;

public interface LimesService extends Remote{
	/**
	 * 
	 * @param source HashMap <table>
	 * 	<tr><td>endpoint</td><td>String</td></tr>
	 *  <tr><td>graph</td><td>String</td></tr>
	 *  <tr><td>prefix</td><td>HashMap "<"String, String">"</td></tr>
	 *  <tr><td>properties</td><td>HashMap "<"String, String">"</td></tr>
	 *  <tr><td>var</td><td>String</td></tr>
	 *  <tr><td></td><td></td></tr>
	 *  </table>
	 * @param targete HashMap <table>
	 * 	<tr><td>endpoint</td><td>String</td></tr>
	 *  <tr><td>graph</td><td>String</td></tr>
	 *  <tr><td>prefix</td><td>HashMap "<"String, String">"</td></tr>
	 *  <tr><td>properties</td><td>HashMap "<"String, String">"</td></tr>
	 *  <tr><td>var</td><td>String</td></tr>
	 *  <tr><td></td><td></td></tr>
	 *  </table>
	 * @param metrice HashMap <table>
	 *  <tr>metric<td></td><td>String</td></tr>
	 *  <tr>accthreshold<td></td><td>Double</td></tr>
	 *  <tr>verthreshold<td></td><td>Double</td></tr>
	 *  </table>
	 */
	public void getMapping(HashMap<String, Object> source, HashMap<String, Object> target, HashMap<String, Object> metric);
}
