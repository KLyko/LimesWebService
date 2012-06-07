package de.uni_leipzig.simba.limeswebservice.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
	 
	
	public int startSession (String emailAddress){
		String con = emailAddress+System.currentTimeMillis();
		int id = con.hashCode();
		LimesUser lu = new LimesUser(id,emailAddress);
		 UserManager.getInstance().addUser(id, lu);
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
		return  UserManager.getInstance().containUser(sessionId);
	}
	
	public String fetchSourceData (int sessionId){
		LimesUser lu = UserManager.getInstance().getUser(sessionId);
		return JsonParser.parseJavaToJSON(lu.getSourceMap());
	}
	
	public String fetchTargetData (int sessionId){
		LimesUser lu = UserManager.getInstance().getUser(sessionId);
		return JsonParser.parseJavaToJSON(lu.getTargetMap());
	}
	
	public String fetchMetricMap (int sessionId){
		LimesUser lu = UserManager.getInstance().getUser(sessionId);
		String result = ""; 
		return JsonParser.parseJavaToJSON(lu.getMetricMap());
	}
	
	public void setSpecification (int sessionId,String source, String target){
		try {
			HashMap<String,Object> sourceMap = JsonParser.parseJSONToJava(source);
			HashMap<String,Object> targetMap = JsonParser.parseJSONToJava(target);
			LimesUser lu = UserManager.getInstance().getUser(sessionId);
			System.out.println(lu);
			lu.setSourceMap(sourceMap);
			
			lu.setTargetMap(targetMap);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setMetricSpec (int sessionId, String metricMap){
		try {
			HashMap<String,Object> metricJMap = JsonParser.parseJSONToJava(metricMap);
			LimesUser lu = UserManager.getInstance().getUser(sessionId);
			lu.setMetricMap(metricJMap);
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
			Properties mailConf = readConf();
		
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
