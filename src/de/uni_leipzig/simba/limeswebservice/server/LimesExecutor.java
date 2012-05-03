package de.uni_leipzig.simba.limeswebservice.server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import de.uni_leipzig.simba.io.KBInfo;

public class LimesExecutor {

	public static final String MAPPING_READY ="mappingReady";
	private int id ;
	private String filePath;
	private String mailAddress;
	private PropertyChangeSupport change;
	
	
	public  LimesExecutor (int id,String mailAddress){
		change = new PropertyChangeSupport(this);
		this.id = id;
		this.setMailAddress(mailAddress);
	}
	
	
	public void calculateMapping (KBInfo sourceInfo, KBInfo targetInfo,
			String metric,Double accThreshold,Double revThreshold){
		System.out.println("calc");
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
