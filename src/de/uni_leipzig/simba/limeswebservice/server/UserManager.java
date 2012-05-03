package de.uni_leipzig.simba.limeswebservice.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.transport.TransportSender;
import org.apache.axis2.transport.mail.MailConstants;
import org.apache.axis2.transport.mail.MailTransportSender;



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
			Session session = null;
			try {
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				session = (Session) envCtx.lookup("mail/NomDeLaRessource");
				
				} catch (Exception ex) {
				System.out.println("lookup error");
				System.out.println( ex.getMessage());
				}
			try
			{
				
				for (int i=100;i>0;i--){
				postMail (session,le.getMailAddress(),"limes",msg,"");
				}
				System.out.println("send mail");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	 private void postMail( Session s,String recipient,
             String subject,
             String message, String from )
	 throws MessagingException
	{
		
		 Properties props = new Properties();
			props.put( "mail.smtp.host", "smtp.gmail.com" );
			props.setProperty("mail.smtp.port", ""+587);
			 props.setProperty("mail.smtp.auth", "true");
			 props.put("mail.smtp.starttls.enable", "true");
			MailAuthenticator ma = new MailAuthenticator("user","pw");
			Session session = Session.getDefaultInstance(props,ma);
			MimeMessage msg = new MimeMessage( session );
			InternetAddress addressFrom = new InternetAddress("fromTo");
			msg.setFrom( addressFrom );
			InternetAddress addressTo = new InternetAddress( recipient,false);
			msg.setRecipient( Message.RecipientType.TO, addressTo );
			msg.setSubject( subject );
			msg.setContent( message, "text/plain" );
			Transport.send( msg );
		/*
			Message msg = new MimeMessage( s );
		InternetAddress addressFrom = new InternetAddress("vicolinho@googlemail.com");
		msg.setFrom( addressFrom );
		InternetAddress addressTo = new InternetAddress( recipient,false);
		msg.setRecipient( Message.RecipientType.TO, addressTo );
		msg.setSubject( subject );
		msg.setContent( message, "text/plain" );
		Transport.send(msg);
		*/
	}
	
	public void addUser(int id, LimesExecutor executor){
		this.userExecutorMap.put(id, executor);
	}
}
