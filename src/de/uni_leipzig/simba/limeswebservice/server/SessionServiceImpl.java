package de.uni_leipzig.simba.limeswebservice.server;

import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

import de.uni_leipzig.simba.limeswebservice.util.JsonParser;

public class SessionServiceImpl {

	private static final Logger log = Logger.getLogger(SessionServiceImpl.class);
	private UserManager uMan = UserManager.getInstance(); 
	
	public int startSession (String emailAddress){
		String con = emailAddress+System.currentTimeMillis();
		int id = con.hashCode();
		LimesUser lu = new LimesUser(id,emailAddress);
		uMan.addUser(id, lu);
		log.info("new Client with id"+id);
		String msg = "Your session id is "+ id +".\n"+
		"The session will delete after 2 days";
		try {
			postMail (emailAddress,"session id",msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	public int continueSession (int sessionId){
		return uMan.containUser(sessionId);
	}
	
	public String fetchSourceData (int sessionId){
		LimesUser lu = uMan.getUser(sessionId);
		return JsonParser.parseJavaToJSON(lu.getSourceMap());
	}
	
	public String fetchTargetData (int sessionId){
		LimesUser lu = uMan.getUser(sessionId);
		return JsonParser.parseJavaToJSON(lu.getTargetMap());
	}
	
	public String fetchMetricMap (int sessionId){
		LimesUser lu = uMan.getUser(sessionId);
		String result = ""; 
		return JsonParser.parseJavaToJSON(lu.getMetricMap());
	}
	
	public void setSpecification (int sessionId,String source, String target){
		try {
			HashMap<String,Object> sourceMap = JsonParser.parseJSONToJava(source);
			HashMap<String,Object> targetMap = JsonParser.parseJSONToJava(target);
			LimesUser lu = uMan.getUser(sessionId);
			lu.setSourceMap(sourceMap);
			lu.setTargetMap(targetMap);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			
			MailAuthenticator ma = new MailAuthenticator("user","pw ");
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
}
