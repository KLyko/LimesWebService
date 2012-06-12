package de.uni_leipzig.simba.limeswebservice.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.io.Serializer;
import de.uni_leipzig.simba.io.SerializerFactory;


public class UserManager implements PropertyChangeListener{

	
	private static  UserManager instance;
	private static HashMap<Integer, LimesUser> userExecutorMap;
	private final static String sameAsRelation = "owl:sameAs";
	
	private UserManager (){
		userExecutorMap = new HashMap<Integer, LimesUser>();
		UserGarbageCollector.getInstance();
	}
	
	public static UserManager  getInstance(){
		if (instance == null){
			instance = new UserManager();
		}
		return instance;
	}

	/**
	 * will called, if a {@linkplain LimesUser} calculated the mapping.
	 * The server will send a mail with the result to the specified mail address of
	 * the LimesUser instance
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(LimesUser.MAPPING_READY)){
//			System.out.println ("ready calculation");
			LimesUser le =userExecutorMap.get(evt.getNewValue());
			String msg = "this is a generated mail";
		
			try
			{
				Serializer serializers[] = SerializerFactory.getAllSerializers();
				List<File> serializedFiles = new LinkedList<File>();
				for(Serializer serializer : serializers) {
					String fileName = generateValidFileName((String) le.getSourceMap().get("endpoint")) + "-" +
										generateValidFileName((String) le.getTargetMap().get("endpoint")) + 
										"."+serializer.getFileExtension();
					//serializer.open(fileName);
					HashMap<String, String> prefixes = (HashMap<String, String>)le.getSourceMap().get("prefixes");
											prefixes.putAll((HashMap<String, String>)le.getTargetMap().get("prefixes"));
											prefixes.put(PrefixHelper.getBase(sameAsRelation),
													PrefixHelper.getURI(PrefixHelper.getBase(sameAsRelation)));
					serializer.setPrefixes(prefixes);
					serializer.writeToFile(le.getResult(), sameAsRelation, fileName);
					serializedFiles.add(serializer.getFile(fileName));
				}
//				File f = new File (evt.getNewValue().toString()+".txt");
////				System.out.println(f.getAbsolutePath());
//				FileWriter fw = new FileWriter(f);
//				fw.write(le.getResult().toString());
//				
//				msg = "See email attachement for your mapping results: " +
//				""+evt.getNewValue().toString()+".txt";
//				fw.close();
				postMail (le.getMailAddress(),"limes",msg, serializedFiles);
				
				System.out.println("send mail");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * send  a mail 
	 * @param recipient 
	 * @param subject
	 * @param message text message
	 * @param file path of the mapping result on server
	 * @throws MessagingException
	 */
	 private void postMail( String recipient,
             String subject,
             String message, List<File> serializedFiles )
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
			
			// multipart = body + attachment
			MimeBodyPart messageBodyPart = 
				      new MimeBodyPart();
		    messageBodyPart.setText("Hi, with this email we send you the mapping results " +
		    		"of your Link Specification carried out with LIMES");

		    Multipart multipart = new MimeMultipart();
		    multipart.addBodyPart(messageBodyPart);

		    // the other parts are the file attachments
		    for(File file : serializedFiles) {
		    	String fileAttachment = file.getAbsolutePath();
			    messageBodyPart = new MimeBodyPart();
			    DataSource source =  new FileDataSource(fileAttachment);
			    messageBodyPart.setDataHandler(new DataHandler(source));
			    messageBodyPart.setFileName(fileAttachment);
			    multipart.addBodyPart(messageBodyPart);
		    }
		    // Put parts in message	and send it	   
			msg.setContent(multipart);
			Transport.send( msg );
			// delete this files after they were send.
			for(File file : serializedFiles) {
				file.delete();
			}
		
	}
	
	public void increaseTime (long interval){
		for (LimesUser lu : userExecutorMap.values()){
			lu.setNoUsageTime(lu.getNoUsageTime()+interval);
		}
	}
	 
	public LimesUser getMostInactiveUser(){
			return Collections.max(this.userExecutorMap.values());
	}
	 
	public boolean existUser(){
		return !userExecutorMap.isEmpty();
	}
	public void addUser(int id, LimesUser executor){
		userExecutorMap.put(id, executor);
	}

	public void deleteUser (int id){
		userExecutorMap.remove(id);
	}
	public LimesUser getUser (int sessionId){
		return userExecutorMap.get(sessionId);
	}
	public int containUser(int sessionId){
		if (userExecutorMap.containsKey(sessionId))
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

	/**
	 * Little helper method to remove any non allowed symbols.
	 * @param s
	 * @return
	 */
	public static String generateValidFileName(String s) {
		s = s.replaceAll("http://", "");
		String regex = "/.{0,8}[sparql]";
		s=s.replaceAll(regex, "");
		s = s.replaceAll("/", "_");
		if(s.endsWith("_"))
			return s.substring(0, Math.max(0, s.length()-1));
		return s;
	}

}
