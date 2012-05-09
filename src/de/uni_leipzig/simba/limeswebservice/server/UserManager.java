package de.uni_leipzig.simba.limeswebservice.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
import org.apache.axis2.description.AxisEndpoint;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.transport.TransportSender;
import org.apache.axis2.transport.mail.MailConstants;
import org.apache.axis2.transport.mail.MailTransportSender;



public class UserManager implements PropertyChangeListener{

	
	private static UserManager instance;
	private HashMap<Integer, LimesUser> userExecutorMap;
	
	
	private UserManager (){
		userExecutorMap = new HashMap<Integer, LimesUser>();
	}
	
	public static UserManager  getInstance(){
		if (instance ==null){
			instance = new UserManager();
		}
		return instance;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(LimesUser.MAPPING_READY)){
			System.out.println ("ready calculation");
			LimesUser le =this.userExecutorMap.get(evt.getNewValue());
			String msg = "this is a generated mail";
		
			try
			{
				File f = new File ("webapps/axis2/"+evt.getNewValue().toString()+".txt");
				System.out.println(f.getAbsolutePath());
				FileWriter fw = new FileWriter(f);
				fw.write(le.getResult().toString());
				msg = " The result is available on http://139.18.249.11:8080/" +
				"axis2/"+evt.getNewValue().toString()+".txt";
				fw.close();
				postMail (le.getMailAddress(),"limes",msg);
				
				System.out.println("send mail");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	 private void postMail( String recipient,
             String subject,
             String message )
	 throws MessagingException
	{
		
		 Properties props = new Properties();
			props.put( "mail.smtp.host", "smtp.gmail.com" );
			props.setProperty("mail.smtp.port", ""+587);
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			Properties mailConf = readConf();
			System.out.println(mailConf.getProperty("mail")+" "+mailConf.getProperty("pw"));
			MailAuthenticator ma = new MailAuthenticator(
					mailConf.getProperty("mail"),mailConf.getProperty("pw"));
			Session session = Session.getDefaultInstance(props,ma);
			MimeMessage msg = new MimeMessage( session );
			InternetAddress addressFrom = new InternetAddress("from");
			msg.setFrom( addressFrom );
			InternetAddress addressTo = new InternetAddress( recipient,false);
			msg.setRecipient( Message.RecipientType.TO, addressTo );
			msg.setSubject( subject );
			msg.setContent( message, "text/plain" );
			Transport.send( msg );
		
	}
	
	public void addUser(int id, LimesUser executor){
		this.userExecutorMap.put(id, executor);
	}

	public LimesUser getUser (int sessionId){
		return this.userExecutorMap.get(sessionId);
	}
	public int containUser(int sessionId){
		if (this.userExecutorMap.containsKey(sessionId))
			return sessionId;
		else
			return -1;
	}
	
	private Properties readConf (){
		 Properties prop = new Properties();
		 try {
			InputStream is = new FileInputStream("mail.conf.txt");
			prop.load(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
		 
	 }
}
