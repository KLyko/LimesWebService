package de.uni_leipzig.simba.limeswebservice.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UserManager implements PropertyChangeListener{

	
	private static UserManager instance;
	private HashMap<Integer, LimesExecutor> userExecutorMap;
	
	
	private UserManager (){
		userExecutorMap = new HashMap<Integer, LimesExecutor>();
	}
	
	public static UserManager  getInstance(){
		if (instance ==null){
			instance = new UserManager();
		}
		return instance;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(LimesExecutor.MAPPING_READY)){
			System.out.println ("ready");
			LimesExecutor le =this.userExecutorMap.get(evt.getNewValue());
			String msg = "this is a generated mail";
			try {
				postMail (le.getMailAddress(),"limes",msg,"");
				System.out.println("send mail");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	 private void postMail( String recipient,
             String subject,
             String message, String from )
	 throws MessagingException
	{
		Properties props = new Properties();
		props.put( "mail.smtp.host", "smtp.gmail.com" );
		props.setProperty("mail.smtp.port", ""+465);
		 props.setProperty("mail.smtp.auth", "true");
		MailAuthenticator ma = new MailAuthenticator("vicolinho","dise#Che88");
		Session session = Session.getDefaultInstance(props,ma);
		MimeMessage msg = new MimeMessage( session );
		InternetAddress addressFrom = new InternetAddress("vicolinho@googlemail.com");
		msg.setFrom( addressFrom );
		InternetAddress addressTo = new InternetAddress( recipient,false);
		msg.setRecipient( Message.RecipientType.TO, addressTo );
		msg.setSubject( subject );
		msg.setContent( message, "text/plain" );
		Transport.send( msg );
	}
	
	public void addUser(int id, LimesExecutor executor){
		this.userExecutorMap.put(id, executor);
	}
}
