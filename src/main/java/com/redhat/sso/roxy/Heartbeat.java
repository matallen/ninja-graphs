package com.redhat.sso.roxy;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class Heartbeat {
  private static final Logger log = Logger.getLogger(Heartbeat.class);
  private static Timer t;
  private static final long delay=30000l;

  public static void main(String[] asd){
    try{
      Heartbeat.runOnce();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  public static void runOnce(){
    new HeartbeatRunnable().run();
  }
  
  public static void start(long intervalInMs) {
    t = new Timer("ninja-roxy-heartbeat", false);
    t.scheduleAtFixedRate(new HeartbeatRunnable(), delay, intervalInMs);
  }

  public static void stop() {
    t.cancel();
  }
  

  static class HeartbeatRunnable extends TimerTask {
    static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    
    @Override
    public void run() {
      log.info("Heartbeat fired");
      
    }    
  }
  
  
  

}
