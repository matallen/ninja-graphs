package com.redhat.sso.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParamParser{

	public Map<String, String> splitParams(String paramString) throws UnsupportedEncodingException{
		Map<String, String> result=new LinkedHashMap<String, String>();
		String[] pairs=paramString.split(",");
		for (String p : pairs){
			int i=p.indexOf("=");
			result.put(URLDecoder.decode(p.substring(0, i), "UTF-8"), URLDecoder.decode(p.substring(i+1), "UTF-8"));
		}
		return result;
	}
}
