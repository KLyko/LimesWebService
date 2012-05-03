package de.uni_leipzig.simba.limeswebservice.server;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator{

	private String user ;
	private String password;
	
	public MailAuthenticator (String user,String pw){
		this.user =user;
		this.password =pw;
	}
	
	@Override
	  public PasswordAuthentication getPasswordAuthentication()
	    {
	        return new PasswordAuthentication(user, password);
	    }
}
