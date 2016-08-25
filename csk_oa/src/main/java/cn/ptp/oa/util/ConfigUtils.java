package cn.ptp.oa.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtils {
	private static Logger log = LoggerFactory.getLogger(ConfigUtils.class);
	private static Properties p  = new Properties();
	static{
	    try {
			p.load(ConfigUtils.class.getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			log.info("load wxqyh.properties failed",e);
		}
	}
	
	public static String getConfigValue(String key){
		return p.getProperty(key);
	}
}
