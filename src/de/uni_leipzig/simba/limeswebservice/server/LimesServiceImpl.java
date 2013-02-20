package de.uni_leipzig.simba.limeswebservice.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.limeswebservice.util.JsonParser;
import de.uni_leipzig.simba.selfconfig.ComplexClassifier;
import de.uni_leipzig.simba.selfconfig.MeshBasedSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.SimpleClassifier;

public class LimesServiceImpl {

	org.slf4j.Logger logger = LoggerFactory.getLogger(LimesServiceImpl.class);
	
	private static final Logger log = Logger.getLogger(LimesServiceImpl.class);
	 
	public int polling (){
		return 1;
	}
	
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
//		String result = ""; 
		return JsonParser.parseJavaToJSON(lu.getMetricMap());
	}
	
	public void setSpecification (int sessionId,String source, String target){
		try {
			HashMap<String,Object> sourceMap = JsonParser.parseJSONToJava(source);
			HashMap<String,Object> targetMap = JsonParser.parseJSONToJava(target);
			LimesUser lu = UserManager.getInstance().getUser(sessionId);
			lu.setSourceMap(sourceMap);
			lu.setTargetMap(targetMap);
			lu.setNoUsageTime(0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setMetricSpec (int sessionId, String metricMap){
		try {
			HashMap<String,Object> metricJMap = JsonParser.parseJSONToJava(metricMap);
			LimesUser lu = UserManager.getInstance().getUser(sessionId);
			log.info(metricMap);
			lu.setMetricMap(metricJMap);
			lu.setNoUsageTime(0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getMapping(int sessionID){
		LimesUser le =UserManager.getInstance().getUser(sessionID);
		le.addPropertyChangeListener(UserManager.getInstance());
		
		
		HashMap<String, Object> source;
		
			source = le.getSourceMap();
		
		HashMap<String,Object> target= le.getTargetMap();
		HashMap<String,Object> metric= le.getMetricMap();
		
		KBInfo sourceInfo = le.createKBInfo(source);
		KBInfo targetInfo = le.createKBInfo(target);
		// get metric
		
		String metricExpr = (String) metric.get("metric");
		Double accThreshold = (Double) metric.get("accthreshold");
		Double verThreshold = (Double) metric.get("verthreshold");
		le.calculateMapping(sourceInfo, targetInfo, metricExpr, accThreshold, verThreshold);
		le.setNoUsageTime(0);		
	}
	
	/**
	 * As we have to work around serializing Hashmaps, we expect to receive JSON parsed String
	 * of the training data Mappings
	 * @param sessionID
	 * @param trainingData
	 * @return
	 */
	public boolean learnMetric(int sessionID, String trainingData) {
		LimesUser le =UserManager.getInstance().getUser(sessionID);
		return le.learn(trainingData);		
	}
	
	
	public String getMetricAdvice (int sessionId){
		String metric ="";
		logger.info( ""+sessionId);
		LimesUser lu = UserManager.getInstance().getUser(sessionId);
		System.out.println(lu);
		KBInfo sourceInfo = lu.createKBInfo (lu.getSourceMap());
		KBInfo targetInfo = lu.createKBInfo (lu.getTargetMap());
		if(sourceInfo.prefixes == null)
			sourceInfo.prefixes = new HashMap<String, String>();
		if(targetInfo.prefixes == null)
			targetInfo.prefixes = new HashMap<String, String>();
		logger.info("getMetricAdvice(): Getting caches...");
		String folder = System.getProperty("java.io.tmpdir");
		HybridCache sC = HybridCache.getData(new File(folder), sourceInfo);
		HybridCache tC = HybridCache.getData(new File(folder), targetInfo);
		logger.info("getMetricAdvice(): Start SelfConfig...");
		MeshBasedSelfConfigurator mbsc = new MeshBasedSelfConfigurator (sC,tC,0.6,0.5);
		List <SimpleClassifier> classifierList = mbsc.getBestInitialClassifiers();
		if (classifierList.size()>0){
			classifierList  = mbsc.learnClassifer(classifierList);
		}
		ComplexClassifier compC = mbsc.getZoomedHillTop(5,5, classifierList);
		//TODO: wait for Limes update to do this more convenient
		metric = this.generateMetric( compC.classifiers, "", sourceInfo, targetInfo);
		lu.setNoUsageTime(0);
		logger.info("getMetricAdvice(): metric=\n"+metric);

		String msg = "Metric advice for your session "+sessionId+" on  "+sourceInfo.id+" - "+targetInfo.id+" is:\n";
		msg += metric;
		try {
			postMail(lu.getMailAddress(), "Metric Advice", msg);
		} catch (MessagingException e) {
			System.err.println("Error sending metric advice via email...");
			e.printStackTrace();
		}
		
		return metric;
	}
	
	
	private String generateMetric(SimpleClassifier sl,KBInfo source,KBInfo target) {
		
		String metric = ""; //$NON-NLS-1$
		
		metric += sl.measure+"("+source.var.replaceAll("\\?", "")+"."+sl.sourceProperty; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		metric +=","+target.var.replaceAll("\\?", "")+"."+sl.targetProperty+")|"+sl.threshold; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		return metric;
	}

	private String generateMetric(List<SimpleClassifier> originalCCList, String expr,KBInfo source,KBInfo target) {
		// need to copy them
		List<SimpleClassifier> sCList = new LinkedList<SimpleClassifier>();
		for(SimpleClassifier sC: originalCCList)
			sCList.add(sC);
		
		if(sCList.size()==0)
			return expr.substring(0, expr.lastIndexOf("|"));
		if(expr.length() == 0) {// nothing generated before
			if(sCList.size()==1) {
				String metric = generateMetric(sCList.get(0), source, target);
				return metric.substring(0, metric.lastIndexOf("|"));
			}
			else {// recursive
				String nestedExpr = "AND("+generateMetric(sCList.remove(0), source, target)+","+generateMetric(sCList.remove(0), source, target)+")|0.0";
				return generateMetric(sCList, nestedExpr, source, target);
			}
		} else { // have to combine, recursive
			String nestedExpr = "AND("+expr+","+generateMetric(sCList.remove(0),source, target)+")|0.0";
			return generateMetric(sCList, nestedExpr, source, target);			
		}
	}
	
	/**
	 * method sends mail.
	 * @param recipient
	 * @param subject
	 * @param message
	 * @throws MessagingException
	 */
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
		 Properties prop = UserManager.readConf();//new Properties();
//		 try {
//			 UserManager.readConf();
//			InputStream is = new FileInputStream("mail.conf.txt");
//			prop.load(is);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return prop;
		 
	 }
}

