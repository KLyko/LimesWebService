package de.uni_leipzig.simba.limeswebservice.server;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class UserGarbageCollector  {
	
	private static final Logger log = Logger.getLogger(UserGarbageCollector.class);
	private Timer  timer ;
	private TimerTask userDeleteTask;
	
	private static UserGarbageCollector instance ;
	
	private UserGarbageCollector (){
		timer = new Timer();
		this.initTimerTask();
		timer.schedule(userDeleteTask, 0, INTERVAL);
		
		
	}
	public static final long WAIT_TIME = 120*1000;
	public static final long INTERVAL = 30*1000;
	
	
	public static UserGarbageCollector getInstance (){
		if (instance ==null){
			instance = new UserGarbageCollector ();
		}
		return instance;
	}
	
	private void initTimerTask (){
		if (userDeleteTask ==null){
			userDeleteTask = new TimerTask (){

				@Override
				public void run() {
					if (UserManager.getInstance().existUser()){
						LimesUser lu = UserManager.getInstance().getMostInactiveUser();
						if (lu.getNoUsageTime()>WAIT_TIME){
							UserManager.getInstance().deleteUser(lu.getId());
							log.info("delete User: "+lu.getId());
						}
							UserManager.getInstance().increaseTime(INTERVAL);
					}
				}
				
			};
		}
	}
}
