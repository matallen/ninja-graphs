package com.redhat.sso.roxy;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.redhat.sso.utils.Json;

@Path("/")
public class Controller {
  private static final Logger log=Logger.getLogger(Controller.class);
  
  public static void main(String[] asd) throws JsonGenerationException, JsonMappingException, IOException{
  }
  
  @POST
  @Path("/proxy/{key}")
  public Response proxySave(@Context HttpServletRequest request, @Context HttpServletResponse response, @Context ServletContext servletContext, @PathParam("key") String key){
    try{
      log.debug("[POST] /proxy Called");
      String raw=IOUtils.toString(request.getInputStream());
      
      Database db=Database.get();
      db.getData().put(key, new String(Base64.encodeBase64(raw.getBytes())));
      db.save();
      return Response.status(200).entity("{\"status\":\"COMPLETE\"}").build();
    }catch(IOException e){
      e.printStackTrace();
      return Response.status(500).entity("{\"status\":\"ERROR\",\"message\":\""+e.getMessage()+"\"}").build();  
    }
  }
  
  // manually (via rest) to increment the points for a specific user and pool id
  @GET
  @Path("/proxy/{key}")
  
  public Response proxyLoad(@Context HttpServletRequest request, @Context HttpServletResponse response, @Context ServletContext servletContext, @PathParam("key") String key){
    try{
      Database db=Database.get();
      String base64Encoded=db.getData().get(key);
      log.info("key="+key);
      log.info("encoded="+base64Encoded);
      if (null!=base64Encoded){
      	String decoded=new String(Base64.decodeBase64(base64Encoded));
      	log.info("decoded and returning="+decoded);
      	return Response.status(200)
      			.entity(decoded)
      			.header("Access-Control-Allow-Origin", "*")
      			.header("Access-Control-Allow-Headers", "origin, context-type, accept, authorization")
      			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
      			.header("content-type", "application/json")
      			.build();
      }else{
      	log.error("No data found for provided key");
      	return Response.status(500).entity("{\"status\":\"ERROR\",\"message\":\"No data found for provided key\"}").build();
      }
    }catch(Exception e){
    	e.printStackTrace();
      return Response.status(500).entity("{\"status\":\"ERROR\",\"message\":\""+e.getMessage()+"\"}").build();  
    }
  }
  
  @GET
  @Path("/database/get")
  public Response databaseGet() throws JsonGenerationException, JsonMappingException, IOException{
  	return Response.status(200).entity(Json.newObjectMapper(true).writeValueAsString(Database.get())).build();
  }
  
  @GET
  @Path("/database/clear")
  public Response databaseClear() throws JsonGenerationException, JsonMappingException, IOException{
  	Database.clear();
  	return Response.status(200).entity(Json.newObjectMapper(true).writeValueAsString(Database.get())).build();
  }
  
//  @GET
//  @Path("/config/get")
//  public Response configGet(@Context HttpServletRequest request,@Context HttpServletResponse response,@Context ServletContext servletContext) throws JsonGenerationException, JsonMappingException, IOException{
//    return Response.status(200).entity(Json.newObjectMapper(true).writeValueAsString(Config.get())).build();
//  }
//  
//  @POST
//  @Path("/config/save")
//  public Response configSave(@Context HttpServletRequest request,@Context HttpServletResponse response,@Context ServletContext servletContext) throws JsonGenerationException, JsonMappingException, IOException{
//    log.info("Saving config");
//    Config newConfig=Json.newObjectMapper(true).readValue(request.getInputStream(), Config.class);
//    
//    log.debug("New Config = "+Json.newObjectMapper(true).writeValueAsString(newConfig));
//    newConfig.save();
//    
//    // re-start the heartbeat with a new interval
//    //TODO: reset the heartbeat ONLY if the interval changed from what it was before
//    String heartbeatInterval=newConfig.getOptions().get("heartbeat.intervalInSeconds");
//    if (null!=heartbeatInterval && heartbeatInterval.matches("\\d+")){
//      log.info("Re-setting heartbeat with interval: "+heartbeatInterval);
//      Heartbeat2.stop();
//      Heartbeat2.start(Long.parseLong(heartbeatInterval));
//    }
//    
//    String maxEvents=newConfig.getOptions().get("events.max");
//    if (null!=maxEvents && maxEvents.matches("\\d+")){
//      Database2.MAX_EVENT_ENTRIES=Integer.parseInt(maxEvents);
//    }
//    
//    log.debug("Saved");
//    return Response.status(200).entity(Json.newObjectMapper(true).writeValueAsString(Config.get())).build();
//  }
  
}
