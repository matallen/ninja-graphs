package com.redhat.sso.roxy;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.attribute.PosixFilePermission;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.io.IOUtils;

public class InitServlet extends HttpServlet {
	
  public static void main(String[] asd) throws ServletException{
    new InitServlet().init(null);
  }
  
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    
//    String intervalString=(String)Config.get().getOptions().get("heartbeat.intervalInSeconds");
//    if (null==intervalString) intervalString="60000";
//    int interval=Integer.parseInt(intervalString);
//    boolean heartbeatDisabled="true".equalsIgnoreCase((String)Config.get().getOptions().get("heartbeat.disabled"));
    
//    System.out.println("Heartbeat:");
//    System.out.println("  Disabled: "+heartbeatDisabled);
//    System.out.println("  Interval: "+interval +" (seconds)");
    
    Database.get();
    
//    if (!heartbeatDisabled)
//      Heartbeat.start(interval);
    
  }

  @Override
  public void destroy() {
    super.destroy();
//    Heartbeat.stop();
//    Backup.stop();
  }

}