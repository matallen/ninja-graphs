package com.redhat.sso.roxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.redhat.sso.utils.IOUtils2;
import com.redhat.sso.utils.Json;

public class Database{
  private static final Logger log=Logger.getLogger(Database.class);
  public static final String STORAGE="target/ninja-persistence/db-roxy.json";
  public static final File STORAGE_AS_FILE=new File(STORAGE);
  public static Integer MAX_EVENT_ENTRIES=1000;
  
  private Map<String, String> data;
  
  // PoolId -> UserId + Score
  private String created;
  private String version;
  static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
  static SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
  
  public Database(){
    created=sdf.format(new Date());
  }
  public String getCreated(){ return created; }
  public String getVersion(){ return version; }
  public void setVersion(String version){
  	this.version=version;
  }
  
  
  public Map<String, String> getData(){
    if(null==data) data=new HashMap<String, String>();
    return data;
  }
  
  public synchronized void save(){
    try{
      long s=System.currentTimeMillis();
      if (!new File(STORAGE).getParentFile().exists())
        new File(STORAGE).getParentFile().mkdirs();
      IOUtils2.writeAndClose(Json.newObjectMapper(true).writeValueAsBytes(this), new FileOutputStream(new File(STORAGE)));
      log.info("Database saved ("+(System.currentTimeMillis()-s)+"ms)");
    }catch (JsonGenerationException e){
      e.printStackTrace();
    }catch (JsonMappingException e){
      e.printStackTrace();
    }catch (FileNotFoundException e){
      e.printStackTrace();
    }catch (IOException e){
      e.printStackTrace();
    }
  }
  
  public static synchronized Database load(){
    try{
      Database db=Json.newObjectMapper(true).readValue(IOUtils2.toStringAndClose(new FileInputStream(new File(STORAGE))), Database.class);
      return db;
    }catch (JsonParseException e){
      e.printStackTrace();
    }catch (JsonMappingException e){
      e.printStackTrace();
    }catch (FileNotFoundException e){
      e.printStackTrace();
    }catch (IOException e){
      e.printStackTrace();
    }
    return null;
  }
  
  private static Database instance=null;
  public static Database getCached(){
    if (null==instance){
      instance=Database.get();
    }
    return instance;
  }
  public static Database get(){
    if (!new File(STORAGE).exists())
      new Database().save();
    instance=Database.load();
    return instance;
  }
  
}
